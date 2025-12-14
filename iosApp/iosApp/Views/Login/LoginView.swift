import SwiftUI
import Shared

struct LoginView: View {
    @State var viewModel = ViewModel()
    
    var body: some View {
        List {
            TextField("Login", text: self.$viewModel.login)         .autocorrectionDisabled().textInputAutocapitalization(.none)
            SecureField("Password", text: self.$viewModel.password) .autocorrectionDisabled().textInputAutocapitalization(.none)
            
            HStack {
                Button("Confirm") { self.viewModel.proceed() }
            }
        }
        .navigationTitle("Log in to S*nergia")
    }
}

extension LoginView {
    @Observable
    class ViewModel: RefreshableViewModel {
        var login = ""
        var password = ""
        
        let repos = RepositoryHelper()
        
        func proceed() {
            self.spawnTask {
                try await self.repos.clientManager.login(login: self.login, password: self.password)
            }
        }
    }
}
