import Shared
import SwiftUI

extension AttendanceSummaryView {
    @Observable
    class ViewModel {
        var overall: String?
        var overallTask: Task<(), Never>?
        
        var semestral = [(Int, String)]()
        var semestralTask: Task<(), Never>?
        
        let repos = RepositoryHelper()
        
        init() {
            self.overallTask = Task {
                for await overall in self.repos.attendanceRepository.getAttendancePercentage() {
                    self.overall = String(format: "%.02f%%", overall.doubleValue)
                }
            }
            
            self.semestralTask = Task {
                for await semestral in self.repos.attendanceRepository.getAttendancePercentageBySemester() {
                    self.semestral = semestral.map { semestral in
                        (Int(semestral.semester), String(format: "%.02f%%", semestral.percentage))
                    }
                }
            }
        }
        
        deinit {
            self.overallTask?.cancel()
            self.semestralTask?.cancel()
        }
    }
}
