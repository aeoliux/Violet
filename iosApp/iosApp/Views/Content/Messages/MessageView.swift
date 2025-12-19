import Shared
import SwiftUI

struct MessageView: View {
    @State var viewModel: ViewModel
    
    init (_ label: MessageLabel_) {
        self.viewModel = ViewModel(label)
    }
    
    var body: some View {
        List {
            ForEach(self.viewModel.tabs, id: \.1) { (icon, label, value) in
                if let value = value {
                    VStack(alignment: .leading, spacing: 12) {
                        HStack {
                            Image(systemName: icon)
                            Text(label)
                        }
                        
                        Text(value)
                    }
                }
            }
        }
        .toolbar {
            if let message = self.viewModel.message, let messageLabel = self.viewModel.messageLabel {
                ToolbarItem(placement: .topBarTrailing) {
                    NavigationLink(value: MessageEditorRoute(label: messageLabel, message: message)) {
                        Image(systemName: "arrowshape.turn.up.backward")
                    }
                }
            }
        }
    }
}
