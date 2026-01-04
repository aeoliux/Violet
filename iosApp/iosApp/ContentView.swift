import SwiftUI
import Shared

struct ContentView: View {
    @State var viewModel = ViewModel()
    
    var body: some View {
        VStack {
            if self.viewModel.logState {
                MainContentView()
            } else {
                LoginView()
            }
        }
        .sheet(isPresented: self.$viewModel.alertShown) {
            List {
                Section("Crash log") {
                    Text(verbatim: self.viewModel.alertState.alertState.message)
                    Button("Copy") { UIPasteboard.general.string = self.viewModel.alertState.alertState.message }
                }
                
                Button("Ok", role: .cancel) { self.viewModel.alertState.alertState.close() }
            }
        }
    }
}

extension ContentView {
    @Observable
    class ViewModel {
        var logState = false
        var alertShown = false
        
        let alertState = AlertStateInjector()
        let repos = RepositoryHelper()
        
        var logStateTask: Task<(), Never>?
        var alertStateTask: Task<(), Never>?
        
        init() {
            self.logStateTask = Task {
                for await logState in self.repos.clientManager.logStateFlow {
                    self.logState = logState.boolValue
                }
            }
            
            self.alertStateTask = Task {
                for await alertShown in self.alertState.alertState.shown {
                    self.alertShown = alertShown.boolValue
                }
            }
        }
        
        deinit {
            self.logStateTask?.cancel()
            self.alertStateTask?.cancel()
        }
    }
}
