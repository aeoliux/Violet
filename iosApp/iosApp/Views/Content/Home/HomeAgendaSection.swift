import Shared
import SwiftUI

struct HomeAgendaSection: View {
    let agenda: [(SwiftUI.Color, Agenda)]
    
    var body: some View {
        Section("Upcoming events") {
            ForEach(self.agenda, id: \.1.id) { (color, agenda) in
                VStack(alignment: .leading, spacing: 5) {
                    Text(prettyDateFormat.string(from: agenda.date.toNSDate()))
                        .font(.caption)
                    
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
