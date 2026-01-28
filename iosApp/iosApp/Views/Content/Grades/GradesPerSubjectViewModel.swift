import Shared
import SwiftUI

extension GradesPerSubjectView {
    @Observable
    class ViewModel: RefreshableViewModel {
        var grades = GradesSummary(final: nil, finalProposal: nil, bySemester: [])
        var gradesTask: Task<(), Never>?
        
        let repos = RepositoryHelper()
        
        var subject: String
        init(_ subject: String) {
            self.subject = subject
            
            super.init()
            
            self.gradesTask = Task {
                for await grades in self.repos.gradesRepository.getGradesFlow(subject: subject) {
                    let semestral = await grades
                        .enumerated()
                        .map { $0 }
                        .mapAsync { (semester, grades) in
                            let average = try await self.repos.gradesRepository.getNonFlowAveragesForSubjectAndSemester(
                                subject: subject,
                                semester: Int32(semester + 1)
                            )
                            
                            return Semester(
                                semester: semester + 1,
                                average: (average.average.averageToColor(), String(format: "%.2f", average.average)),
                                semestralProposal: grades
                                    .first { $0.gradeType == .semesterProposition }?
                                    .toGradeInfo(),
                                semestralFinal: grades
                                    .first { $0.gradeType == .semester }?
                                    .toGradeInfo(),
                                consituent: grades
                                    .filter { $0.gradeType == .constituent }
                                    .map { $0.toGradeInfo() }
                            )
                        }
                        .reversed()
                    
                    self.grades = GradesSummary(
                        final: grades
                            .compactMap { bySemester in
                                bySemester
                                    .first { $0.gradeType == .final }?
                                    .toGradeInfo()
                            }
                            .first,
                        finalProposal: grades
                            .compactMap { bySemester in
                                bySemester
                                    .first { $0.gradeType == .finalProposition }?
                                    .toGradeInfo()
                            }
                            .first,
                        bySemester: semestral.map { $0 }
                    )
                }
            }
        }
        
        deinit {
            self.gradesTask?.cancel()
        }
        
        func refresh() async {
            _ = await self.task {
                try await self.repos.gradesRepository.refresh()
            }
        }
        
        struct GradesSummary {
            let final: GradeInfo?
            let finalProposal: GradeInfo?
            let bySemester: [Semester]
        }
        
        struct Semester {
            let semester: Int
            let average: (SwiftUI.Color, String)
            let semestralProposal: GradeInfo?
            let semestralFinal: GradeInfo?
            let consituent: [GradeInfo]
        }
        
        struct GradeInfo: Hashable, Equatable {
            let color: SwiftUI.Color
            let grade: Grade_
        }
    }
}

extension Grade_ {
    func toGradeInfo() -> GradesPerSubjectView.ViewModel.GradeInfo {
        return GradesPerSubjectView.ViewModel.GradeInfo(color: self.color.toColor() ?? Color.primary, grade: self)
    }
}

extension Array {
    func mapAsync<D>(_ transform: (_ v: Element) async throws -> D) async -> [D] {
        var out = [D]()
        
        for item in self {
            if let item = try? await transform(item) {
                out.append(item)
            }
        }
        
        return out
    }
}
