import Shared
import SwiftUI

struct AttendanceView: View {
    @State var viewModel = ViewModel()
    
    var body: some View {
        List {
            ForEach(self.viewModel.dates, id: \.self) { date in
                if let entries = self.viewModel.attendance[date] {
                    Section(dateFormat.string(from: date)) {
                        ForEach(entries, id: \.id) { entry in
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
                }
            }
        }
        .navigationTitle("Attendance")
        .refreshable { await self.viewModel.refresh() }
    }
}

let dateFormat = {
    let f = DateFormatter()
    f.dateFormat = "dd MMMM YYYY"
    
    return f
}()
