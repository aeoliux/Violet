import Shared
import SwiftUI

struct MenuView: View {
    private let tabs = [
        ("envelope", "Messages", Routes.Messages),
        ("calendar.badge.exclamationmark", "Agenda", Routes.Agenda),
        ("checkmark.seal", "Attendance", Routes.Attendance)
    ]
    
    var body: some View {
        NavigationStack {
            List {
                ForEach(self.tabs, id: \.1) { (icon, label, destination) in
                    NavigationLink(value: destination) {
                        Label(label, systemImage: icon)
                    }
                }
            }
            .navigationTitle("Menu")
            .navigationDestination(for: MessageLabel_.self) { label in
                MessageView(label)
            }
            .navigationDestination(for: MessageEditorRoute.self) { data in
                MessageEditorView(messageLabel: data.label, message: data.message)
            }
            .navigationDestination(for: Routes.self) { route in
                switch route {
                case .Messages:
                    MessagesView()
                case .Agenda:
                    AgendaView()
                case .Attendance:
                    AttendanceView()
                }
            }
        }
    }
}

private enum Routes {
    case Messages
    case Agenda
    case Attendance
}

struct MessageEditorRoute: Hashable, Equatable {
    let label: MessageLabel_?
    let message: Message_?
}
