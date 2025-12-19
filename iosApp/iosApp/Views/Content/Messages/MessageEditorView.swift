import Shared
import SwiftUI

struct MessageEditorView: View {
    let messageLabel: MessageLabel_?
    let message: Message_?
    
    @State var viewModel: ViewModel
    
    init(messageLabel: MessageLabel_? = nil, message: Message_? = nil) {
        self.messageLabel = messageLabel
        self.message = message
        self.viewModel = ViewModel(message: message, messageLabel: messageLabel)
    }
    
    var body: some View {
        List {
            Section("Receivers") {
                ForEach(self.viewModel.selectedUsers, id: \.senderId) { user in
                    Text("\(user.lastName) \(user.firstName)")
                }
                .onDelete { indexes in
                    self.viewModel.selectedUsers.remove(atOffsets: indexes)
                }
                
                if (self.viewModel.respondsTo == nil || self.viewModel.selectedUsers.isEmpty) && !self.viewModel.requestKey.isEmpty && !self.viewModel.allUsers.isEmpty {
                    Button("Choose receivers") { self.viewModel.usersSelector = true }
                }
            }
            
            Section("Message") {
                TextField("Topic", text: self.$viewModel.topic)
                    .lineLimit(1)
                
                TextEditor(text: self.$viewModel.content)
                    .lineLimit(10...)
            }
        }
        .navigationTitle(self.viewModel.respondsTo != nil ? "Respond" : "New message")
        .navigationBarTitleDisplayMode(.inline)
        .toolbar {
            if !self.viewModel.content.isEmpty && !self.viewModel.topic.isEmpty && !self.viewModel.selectedUsers.isEmpty && !self.viewModel.requestKey.isEmpty {
                Button {
                    Task { try await self.viewModel.sendMessage() }
                } label: {
                    Image(systemName: "paperplane")
                }
            }
        }
        .sheet(isPresented: self.$viewModel.usersSelector) {
            UsersSelectorView(self.viewModel.allUsers, alreadySelected: self.viewModel.selectedUsers) {
                self.viewModel.selectedUsers = $0
            }
        }
    }
}
