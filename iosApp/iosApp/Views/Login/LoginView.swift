import SwiftUI
import Shared

struct LoginView: View {
    @State var viewModel = ViewModel()
    
    var body: some View {
        NavigationStack {
            List {
                Section {
                    TextField("Login", text: self.$viewModel.login)         .autocorrectionDisabled().textInputAutocapitalization(.never)
                    SecureField("Password", text: self.$viewModel.password) .autocorrectionDisabled().textInputAutocapitalization(.never)
                    
                    HStack {
                        TaskButton(title: "Log in") { await self.viewModel.proceed() }
                    }
                } header: {
                    Text("Credentials")
                } footer: {
                    Text("Use your S\\*nergia credentials to log in to Violet. Your credentials are only being sent to L\\*brus. Password will be stored in your device's secure keychain.")
                }
            }
            .navigationTitle("Log in to Violet")
        }
    }
}

extension LoginView {
    @Observable
    class ViewModel: RefreshableViewModel {
        var login = ""
        var password = ""
        
        let repos = RepositoryHelper()
        
        func proceed() async {
            _ = try? await self.repos.clientManager.login(login: self.login, password: self.password)
        }
    }
}
