import Shared
import SwiftUI

struct SchoolNoticesView: View {
    @State var viewModel = ViewModel()
    
    var body: some View {
        List {
            ForEach(self.viewModel.notices, id: \.id) { notice in
                Section("\(notice.addedBy), \(minimalDate.string(from: notice.startDate.toNSDate())) - \(minimalDate.string(from: notice.endDate.toNSDate()))") {
                    Text(notice.subject)
                        .font(.headline)
                    
                    Text(notice.content)
                        .font(.subheadline)
                        .textSelection(.enabled)
                }
            }
        }
        .navigationTitle("School notices")
        .refreshable { await self.viewModel.refresh() }
    }
}

let minimalDate = {
    let f = DateFormatter()
    f.dateFormat = "dd.MM"
    return f
}()
