import Shared
import SwiftUI

extension AgendaView {
    @Observable
    class ViewModel: RefreshableViewModel {
        var agenda = [(String, [(Agenda, SwiftUI.Color)])]()
        var agendaTask: Task<(), Never>?
        var monthsBack: Int32 = 0
        var monthsForward: Int32 = 1
        
        let repos = RepositoryHelper()
        
        override init() {
            super.init()
            
            self.actor()
        }
        
        func actor() {
            self.agendaTask?.cancel()
            self.agendaTask = Task {
                for await agenda in self.repos.agendaRepository.getAgendaFlow(monthsBack: self.monthsBack, monthsForward: self.monthsForward) {
                    self.agenda = agenda.compactMap { (date, agenda) in
                        guard
                            let date = (date as? Kotlinx_datetimeLocalDate)?.toNSDate(),
                            let agenda = agenda as? [Agenda]
                        else { return nil }
                        
                        return (
                            prettyDateFormat.string(from: date),
                            agenda.map { agenda in (agenda, agenda.color.toColor() ?? Color.primary) }
                        )
                    }
                }
            }
        }
        
        deinit {
            self.agendaTask?.cancel()
        }
        
        func refresh() async {
            await self.task {
                try await self.repos.agendaRepository.refresh()
            }
        }
    }
}

let prettyDateFormat = {
    let f = DateFormatter()
    f.dateFormat = "EEEE, dd MMMM"
    
    return f
}()
