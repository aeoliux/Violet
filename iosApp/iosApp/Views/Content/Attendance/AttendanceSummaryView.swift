import Shared
import SwiftUI

struct AttendanceSummaryView: View {
    @State var viewModel = ViewModel()
    @Environment(\.dismiss) var dismiss
    
    var body: some View {
        NavigationStack {
            List {
                Section("Overall") {
                    if let overall = self.viewModel.overall {
                        HStack {
                            Text("Whole year")
                            Spacer()
                            Text(overall)
                        }
                    }
                    
                    ForEach(self.viewModel.semestral, id: \.0) { (semester, percentage) in
                        HStack {
                            Text("Semester \(semester)")
                            Spacer()
                            Text(percentage)
                        }
                    }
                }
            }
            .navigationTitle("Summary")
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button {
                        self.dismiss()
                    } label: {
                        Image(systemName: "chevron.up")
                    }
                }
            }
        }
    }
}
