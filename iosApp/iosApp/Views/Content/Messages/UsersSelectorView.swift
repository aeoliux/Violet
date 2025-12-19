import Shared
import SwiftUI

struct UsersSelectorView: View {
    @Environment(\.dismiss) var dismiss
    
    @State var users: [User]
    @State var selectedUsers: Set<Int32>
    let completionHandler: (_ users: [User]) -> Void
    
    init(_ users: [User], alreadySelected: [User], completionHandler: @escaping (_ users: [User]) -> Void) {
        self._users = State(initialValue: users)
        self._selectedUsers = State(initialValue: Set(alreadySelected.map { $0.id }))
        self.completionHandler = completionHandler
    }
    
    var body: some View {
        NavigationView {
            List(self.users, id: \.id, selection: self.$selectedUsers) { user in
                HStack {
                    Text("\(user.lastName) \(user.firstName)")
                    Spacer()
                    Text("(\(user.senderId))")
                        .font(.footnote)
                }
                    .tag(user.id)
            }
            .toolbar {
                ToolbarItem(placement: .bottomBar) {
                    HStack {
                        Spacer()
                        
                        Text("Selected: \(self.selectedUsers.count)")
                        
                        Spacer()
                    }
                }
                
                ToolbarItem(placement: .topBarLeading) {
                    Button("Cancel") { self.dismiss() }
                }
                
                ToolbarItem(placement: .topBarTrailing) {
                    Button("Confirm") {
                        self.completionHandler(
                            self.users
                                .filter { user in self.selectedUsers.contains(user.id) }
                        )
                        
                        self.dismiss()
                    }
                }
            }
            .environment(\.editMode, .constant(.active))
        }
    }
}
