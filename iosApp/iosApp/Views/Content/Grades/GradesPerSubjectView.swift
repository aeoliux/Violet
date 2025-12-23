import Shared
import SwiftUI

struct GradesPerSubjectView: View {
    @State var viewModel: ViewModel
    
    init(_ subject: String) {
        self.viewModel = ViewModel(subject)
    }
    
    var body: some View {
        List {
            ForEach(self.viewModel.grades.bySemester, id: \.semester) { semester in
                if semester.semestralFinal != nil || semester.semestralProposal != nil || semester.average.1 != "0.00" {
                    Section("Semester \(semester.semester) - summary") {
                        if semester.average.1 != "0.00" {
                            HStack {
                                Text("Average")
                                
                                Spacer()
                                
                                AverageComponent(color: semester.average.0, average: semester.average.1)
                            }
                        }
                        
                        if let final = semester.semestralFinal {
                            GradeComponent(grade: final)
                        }
                        
                        if let proposal = semester.semestralProposal {
                            GradeComponent(grade: proposal)
                        }
                    }
                }
                
                if !semester.consituent.isEmpty {
                    Section("Semester \(semester.semester) - grades") {
                        ForEach(semester.consituent, id: \.grade.id) { grade in
                            GradeComponent(grade: grade)
                        }
                    }
                }
            }
            .refreshable { await self.viewModel.refresh() }
        }
        .navigationDestination(for: Grade_.self) { grade in
            GradeView(grade)
        }
    }
}

struct GradeComponent: View {
    let grade: GradesPerSubjectView.ViewModel.GradeInfo
    
    var body: some View {
        NavigationLink(value: grade.grade) {
            HStack {
                VStack(alignment: .leading) {
                    Text(grade.grade.addedBy)
                        .font(.callout)
                    Text(grade.grade.category)
                        .font(.callout)
                }
                
                Spacer()
                
                Rectangle()
                    .fill(grade.color)
                    .frame(width: 40, height: 40)
                    .clipShape(RoundedRectangle(cornerRadius: 5))
                    .overlay {
                        Text("\(grade.grade.grade)")
                            .foregroundStyle(Color.black)
                    }
            }
        }
    }
}
