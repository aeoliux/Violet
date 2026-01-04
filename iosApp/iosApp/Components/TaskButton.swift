import SwiftUI
import Shared

struct TaskButton: View {
    let title: String
    let systemName: String? = nil
    let task: () async throws -> Void
    
    var viewModel = RefreshableViewModel()
    
    var body: some View {
        HStack {
            Button {
                self.viewModel.spawnTask {
                    try await self.task()
                }
            } label: {
                if let systemName = self.systemName {
                    Label(self.title, image: systemName)
                } else {
                    Text(self.title)
                }
            }
            .disabled(self.viewModel.isRefreshing)
            
            Spacer()
            
            if self.viewModel.isRefreshing {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle())
            }
        }
    }
}

struct ToolbarTaskButton: View {
    let systemName: String
    let task: () async throws -> Void
    
    var viewModel = RefreshableViewModel()
    
    var body: some View {
        Button {
            self.viewModel.spawnTask {
                try await self.task()
            }
        } label: {
            if self.viewModel.isRefreshing {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle())
            } else {
                Image(systemName: self.systemName)
            }
        }
        .disabled(self.viewModel.isRefreshing)
    }
}
