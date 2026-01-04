import SwiftUI
import Shared

struct AttendanceComponent: View {
    let entry: Attendance
    
    var body: some View {
        HStack {
            VStack(alignment: .leading) {
                Text(entry.type)
                    .font(.headline)
                
                Text("Lesson no. \(entry.lessonNo)")
                    .font(.caption)
                
                Text("By \(entry.addedBy)")
                    .font(.caption)
            }
            
            Spacer()
            
            Rectangle()
                .fill(entry.color.toColor() ?? SwiftUI.Color.white)
                .frame(width: 40, height: 40)
                .clipShape(RoundedRectangle(cornerRadius: 5))
                .overlay {
                    Text("\(entry.typeShort)")
                        .foregroundStyle(Color.black)
                }
        }
    }
}
