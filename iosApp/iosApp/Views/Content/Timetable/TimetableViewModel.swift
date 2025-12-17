import Shared
import SwiftUI

extension TimetableView {
    @Observable
    class ViewModel: RefreshableViewModel {
        var pathState = [NavKey]()
        
        var timetable = [NavKey]()
        var timetableTask: Task<(), Never>?
        
        let repos = RepositoryHelper()
        
        override init() {
            super.init()
            
            self.timetableTask = Task {
                for await timetable in self.repos.timetableRepository.getTimetableFlow() {
                    self.timetable = timetable.compactMap { (date, timetable) in
                        guard
                            let date = (date as? Kotlinx_datetimeLocalDate)?.toNSDate(),
                            let timetable = timetable as? KotlinMutableDictionary<Kotlinx_datetimeLocalTime, NSArray>
                        else { return nil }
                        
                        return NavKey(
                            date: date,
                            timetable: timetable.compactMap { (time, entries) in
                                guard
                                    let time = (time as? Kotlinx_datetimeLocalTime)?.toNSDate(since: date),
                                    let entries = entries as? [Timetable]
                                else { return nil }
                                
                                return NavKey.Entry(time: time, entries: entries)
                            }
                        )
                    }
                    
                    if self.pathState.isEmpty, let key = (self.timetable.first { Calendar.current.isDateInToday($0.date) } ?? self.timetable.first) {
                        self.pathState.append(key)
                    }
                }
            }
        }
        
        deinit {
            self.timetableTask?.cancel()
        }
        
        func refresh() async {
            await self.task {
                try await self.repos.timetableRepository.refresh()
            }
        }
        
        struct NavKey: Hashable, Equatable {
            let date: Date
            let timetable: [Entry]
            
            struct Entry: Hashable, Equatable {
                let time: Date
                let entries: [Timetable]
            }
        }
    }
}
