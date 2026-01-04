import Shared
import SwiftUI

@Observable
open class RefreshableViewModel {
    var isRefreshing = false
    private let alertState = AlertStateInjector()
    
    func task<T>(_ closure: () async throws -> T) async -> T? {
        do {
            self.isRefreshing = true
            let result = try await closure()
            self.isRefreshing = false
            return result
        } catch {
            self.alertState.alertState.show(message: error.localizedDescription)
            return nil
        }
    }
    
    func spawnTask(_ closure: @escaping () async throws -> Void) {
        Task {
            await self.task(closure)
        }
    }
}
