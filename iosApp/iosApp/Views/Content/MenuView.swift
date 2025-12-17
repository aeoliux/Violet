import Shared
import SwiftUI

struct MenuView: View {
    private let tabs = [
        ("envelope", "Messages", Routes.Messages),
        ("calendar.badge.exclamationmark", "Agenda", Routes.Agenda)
    ]
    
    var body: some View {
        NavigationStack {
            List {
                ForEach(self.tabs, id: \.1) { (icon, label, destination) in
                    NavigationLink(value: destination) {
                        Image(systemName: icon)
                        Text(label)
                    }
                }
            }
            .navigationTitle("Menu")
            .navigationDestination(for: MessageLabel_.self) { label in
                MessageView(label)
            }
            .navigationDestination(for: Routes.self) { route in
                switch route {
                case .Messages:
                    MessagesView()
                case .Agenda:
                    AgendaView()
                }
            }
        }
    }
}

private enum Routes {
    case Messages
    case Agenda
}
