import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        ModulesKt.initializeKoin(
            platformModule: Modules_iosKt.getPlatformModules(
                keychain: Keychain(
                    savePassFunc: PlatformKeychain.savePassFunc,
                    getPassFunc: PlatformKeychain.getPassFunc,
                    deletePassFunc: PlatformKeychain.deletePassFunc
                )
            )
        )
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
