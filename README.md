<h1 align="center">🧳 RecoTrip — Kotlin/Android Native</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Android-3DDC84?style=flat-square&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Firebase-FFCA28?style=flat-square&logo=firebase&logoColor=black" />
  <img src="https://img.shields.io/badge/Google_Places_API-4285F4?style=flat-square&logo=googlemaps&logoColor=white" />
</p>

<br/>

> **4인 팀 프로젝트 "RecoTrip"에서 담당 파트(플랜 생성 플로우 + 앱 설정)를  
> Kotlin/Android Native로 개별 구현한 버전입니다.  
> 이후 팀 전체 코드를 React Native(TypeScript)로 통합했습니다.**

- 팀 통합 버전 (React Native): [jisung-louis/RecoTripJS](https://github.com/jisung-louis/RecoTripJS)
- 개인 독립 실행 버전 (React Native): [ImDabin/RecoTripDB](https://github.com/ImDabin/RecoTripDB)

---

## 📌 프로젝트 개요

AI 기반 해외여행 일정 추천 앱 — 키워드 선택부터 관광지 선택, 날짜별 자동 일정 생성, 숙소 추천, 플랜 저장까지 한 번에 제공합니다.

- **소속:** 성결대학교 컴퓨터공학과 전공종합설계 팀 F4
- **개발 기간:** 2024.09 – 2025.06
- **담당 역할:** 프론트엔드 / 디자인

---

## 👩‍💻 담당 구현 파트

### 플랜 생성 전체 플로우 (9단계)

```
ChooseFragment (시작)
  → KeywordFragment (키워드 선택)
  → CityFragment (도시 추천)
  → CityDetailFragment (도시 상세)
  → LandmarkFragment (관광지 선택)
  → DateFragment / PeopleFragment / FlightFragment (날짜·인원·항공편)
  → RouteFragment (자동 일정 확인)
  → LodgingFragment (숙소 선택)
  → FinalFragment (최종 플랜 저장)
```

### 앱 설정 화면

- 닉네임 변경, 비밀번호 변경, 프로필 사진 설정
- 로그아웃, 회원탈퇴
- 개인정보 처리방침, 이용약관

### 상태 관리

- `PlanViewModel` — 플랜 생성 9단계의 모든 상태를 단일 ViewModel로 관리
- 도시·관광지·날짜·인원·항공편·숙소·경로 데이터 흐름 설계

---

## 🛠️ 기술 스택

| 기술 | 용도 |
|------|------|
| Kotlin | Android 앱 개발 |
| Android XML (View Binding) | UI 레이아웃 |
| Firebase Firestore | 여행 플랜 저장 |
| Firebase Authentication | 로그인 / 회원가입 |
| Google Places API | 관광지 및 숙소 정보 |
| ViewModel / LiveData | MVVM 상태 관리 |
| RecyclerView + Adapter | 리스트 UI |

---

## 📂 프로젝트 구조

```
ui/
├── plan/               # 플랜 생성 전체 플로우 (담당)
│   ├── fragment/
│   │   ├── ChooseFragment.kt
│   │   ├── KeywordFragment.kt
│   │   ├── CityFragment.kt
│   │   ├── CityDetailFragment.kt
│   │   ├── LandmarkFragment.kt
│   │   ├── DateFragment.kt
│   │   ├── PeopleFragment.kt
│   │   ├── FlightFragment.kt
│   │   ├── RouteFragment.kt
│   │   ├── LodgingFragment.kt
│   │   └── FinalFragment.kt
│   └── PlanViewModel.kt
├── settings/           # 앱 설정 화면 (담당)
│   └── SettingsActivity.kt
├── home/               # 홈 화면
├── auth/               # 로그인 / 회원가입
└── board/              # 커뮤니티 게시판
```

---

## 🔗 관련 레포지토리

| 레포 | 설명 |
|------|------|
| [RecoTripDB-Kotlin](https://github.com/ImDabin/RecoTripDB-Kotlin) | 본 레포 — Kotlin 개인 파트 |
| [RecoTripDB](https://github.com/ImDabin/RecoTripDB) | React Native 독립 실행 버전 (서버리스 재구성) |
| [RecoTripJS](https://github.com/jisung-louis/RecoTripJS) | 팀 통합 버전 (React Native) |
