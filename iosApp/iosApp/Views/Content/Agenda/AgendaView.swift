import SwiftUI

struct AgendaView: View {
    @State var viewModel = ViewModel()
    
    var body: some View {
        List {
            ForEach(self.viewModel.agenda, id: \.0) { (date, agenda) in
                Section(date) {
                    ForEach(agenda, id: \.0.id) { (agenda, color) in
                        VStack(alignment: .leading, spacing: 5) {
                            Text(agenda.category)
                                .font(.headline)
                            Text(agenda.content)
                                .font(.subheadline)
                            Text("\(agenda.createdBy), \(agenda.subject ?? "Unknown subject")")
                                .font(.footnote)
                        }
                        .foregroundStyle(color)
                    }
                }
            }
        }
        .navigationTitle("Agenda")
        .refreshable { await self.viewModel.refresh() }
    }
}
