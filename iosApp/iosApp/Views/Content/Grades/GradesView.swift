import Shared
import SwiftUI

struct GradesView: View {
    @State var viewModel = ViewModel()
    
    var body: some View {
        NavigationStack {
            List {
                ForEach(self.viewModel.grades, id: \.subject) { key in
                    NavigationLink(value: key) {
                        Text(key.subject)
                    }
                }
            }
            .refreshable { await self.viewModel.refresh() }
            .navigationTitle("Grades")
            .navigationDestination(for: NavKey.self) { key in
                GradesPerSubjectView(grades: key.grades, refresh: self.viewModel.refresh)
                    .navigationTitle(key.subject)
            }
        }
    }
}
