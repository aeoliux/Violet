import Shared
import SwiftUI

extension SchoolNoticesView {
    @Observable
    class ViewModel {
        let repos = RepositoryHelper()
        
        var notices = [SchoolNotice__]()
        var noticesTask: Task<(), Never>?
        
        init() {
            self.noticesTask = Task {
                for await notices in self.repos.schoolNoticesRepository.getSchoolNoticesFlow() {
                    self.notices = notices
                }
            }
        }
        
        deinit {
            self.noticesTask?.cancel()
        }
        
        func refresh() async {
            _ = try? await self.repos.schoolNoticesRepository.refresh()
        }
    }
}
