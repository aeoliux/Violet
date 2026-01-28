import Shared
import SwiftUI

struct SettingsView: View {
    @State var viewModel = ViewModel()
    
    var body: some View {
        List {
            Section("Account info") {
                Label(self.viewModel.login, systemImage: "number")
                Label("\(self.viewModel.firstName) \(self.viewModel.lastName)", systemImage: "person.circle")
                Label(self.viewModel.email, systemImage: "envelope")
            }
            
            Section {
                TextField("Login", text: self.$viewModel.login)
                SecureField("Password", text: self.$viewModel.password)
                    .autocorrectionDisabled()
                    .textInputAutocapitalization(.never)
                TaskButton(title: "Confirm change") { try await self.viewModel.updateCredentials() }
            } header: {
                Text("Credentials")
            } footer: {
                Text("If you have recently updated your password via website, you might not be able to sync your data. This occurs especially in LiveContainer, where uninstalling the app does not remove old password from keychain, and if you are using shared LiveContainer app, the keychain is not shared between LiveContainer and LiveContainer2, so you need to set your password separately in LiveContainer2.")
            }
        }
        .navigationTitle("Settings")
    }
}

extension SettingsView {
    @Observable
    class ViewModel: RefreshableViewModel {
        var login = ""
        var email = ""
        var password = ""
        
        var firstName = ""
        var lastName = ""
        
        var credentialsTask: Task<(), Never>?
        
        let repos = RepositoryHelper()
        
        override init() {
            super.init()
            
            self.credentialsTask = Task {
                for await credentials in self.repos.aboutMeRepository.getAboutMeFlow() {
                    self.login = credentials.login
                    self.firstName = credentials.firstName
                    self.lastName = credentials.lastName
                    self.email = credentials.email
                }
            }
        }
        
        deinit {
            self.credentialsTask?.cancel()
        }
        
        func refresh() {
            self.spawnTask {
                try await self.repos.fullRefresh()
            }
        }
        
        func updateCredentials() async throws {
            try await self.repos.updateCredentials(login: self.login, password: self.password)
        }
    }
}
