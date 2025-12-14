import Shared

open class RefreshableViewModel {
    private(set) var isRefreshing = false
    private let alertState = AlertStateInjector()
    
    func task<T>(_ closure: () async throws -> T) async -> T? {
        do {
            return try await closure()
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
