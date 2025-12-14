import SwiftUI

struct MainContentView: View {
    var body: some View {
        TabView {
            HomeView()
                .tabItem {
                    Label("Home", systemImage: "house")
                }
            
            GradesView()
                .tabItem {
                    Label("Grades", systemImage: "6.square")
                }
            
            TimetableView()
                .tabItem {
                    Label("Timetable", systemImage: "calendar.badge.clock")
                }
        }
    }
}
