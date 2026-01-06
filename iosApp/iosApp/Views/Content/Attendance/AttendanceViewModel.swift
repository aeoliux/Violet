import Shared
import SwiftUI

extension AttendanceView {
    @Observable
    class ViewModel: RefreshableViewModel {
        var attendanceTask: Task<(), Never>?
        var attendance = [Date : [Attendance]]()
        var dates = [Date]()
        
        var showSummary = false
        
        let repos = RepositoryHelper()
        
        override init() {
            super.init()
            
            self.attendanceTask = Task {
                for await attendance in self.repos.attendanceRepository.getUnattendanceFlow() {
                    self.attendance = attendance.reduce([:]) { acc, entry in
                        var acc = acc
                        let date = entry.date.toNSDate()
                        
                        if acc[date] == nil {
                            acc[date] = []
                        }
                        
                        acc[date]?.append(entry)
                        return acc
                    }
                    
                    self.dates = self.attendance.keys.sorted { (date1, date2) in date1 > date2}
                }
            }
        }
        
        func refresh() async {
            _ = try? await self.repos.attendanceRepository.refresh()
        }
        
        deinit {
            self.attendanceTask?.cancel()
        }
    }
}
