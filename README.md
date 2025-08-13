# ⏱ Timerzeer

**Timerzeer** is an Android app built with Jetpack Compose for managing stopwatches, countdown timers, and custom timer styles.  
It’s designed with a clean Material 3 UI and smooth animations for an intuitive time-tracking experience.

---

## 🚀 Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/timerzeer.git
   cd timerzeer
   ```

2. **Open in Android Studio**
   - Use **Android Studio Flamingo** or newer.
   - Open the project folder.

3. **Sync Gradle**
   - Gradle will automatically download dependencies.

4. **Run the App**
   - Select a connected device or emulator.
   - Press ▶ Run.

---

## ✅ Implemented Features

- **Stopwatch**
  - Start, pause, resume, stop.
  - Real-time elapsed time updates.
  - Shared ViewModel between screens.
  
- **Countdown Timer**
  - Start from a preset time.
  - Pause, resume, stop.
  - Auto-reset after countdown completion.
  
- **UI/UX**
  - Compose Navigation.
  - Smooth arrow animations in TimeSelector.
  - Click outside to unfocus input fields.

---

## ⏳ Pending Features

- [ ] **Timer persistence** on app restart.

### 🎨 Timer persistence
- [ ] Timer continues on app restart.

### 🎨 Customization Features
- [ ] Change Timer Style (apply selected style).
- [ ] Change Timer Background (apply selected background).

### 🎉 Completion Animations
- [ ] Confetti Animation.
- [ ] Ribbon Animation.

### 🔒 Lock Screen Option
- [ ] Lock/unlock mechanism (press and hold for 3 seconds to unlock).

### 📤 Share Functionality
- [ ] Native Android share sheet for sharing timer state.

### ⚡ UI and Performance Enhancements
- [ ] Advanced transitions and animations.
- [ ] Final polish on bottom sheets and interactive elements.

---

## 📌 Notes & Assumptions

- App follows **MVI architecture** with ViewModel state handling.
- Shared ViewModels are scoped to **navigation graph** for state preservation.
- Timer logic uses `CoroutineScope` + `delay` for ticking.
- Designed for **API 24+**.

---

## 📸 Screenshots

| Stopwatch Screen | Countdown Screen | Timer Full Screen |
|------------------|------------------|--------------------------|
|<img width="300" alt="image" src="https://github.com/user-attachments/assets/bbfe665a-cb09-4907-bd86-b6f7d5869447" />|<img width="300" alt="image" src="https://github.com/user-attachments/assets/69d78f1f-d31f-4c8b-b36a-6b11cad17600" />|<img width="300" alt="image" src="https://github.com/user-attachments/assets/a3283d26-5f64-4df4-b874-5212e26516b7" />
" />

---

## 🎥 Demo Video
[timerzeer.webm](https://github.com/user-attachments/assets/1b8293b9-e605-4e9b-bfd3-6464e6114254)

---
