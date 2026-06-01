# MPopUp

popup / dialog kustom untuk Android — konfigurasi judul, pesan, warna, radius, dan tombol lewat builder, atau pakai custom layout sendiri.

## Kompatibilitas / Catatan versi

Mulai `1.0.0` library di-migrasi ke **AndroidX** dengan **minSdk 21** (toolchain modern: AGP 8 / Gradle 8 / JDK 17). Pastikan project konsumer mengaktifkan AndroidX (`android.useAndroidX=true` di `gradle.properties`).

- Penomoran versi memakai **semver** (`1.0.0`, tanpa prefix `v`). Tag lama `v1` adalah build *legacy* berbasis Android Support Library (minSdk 15).
- Public API tidak berubah — `setTitle`, `setMessage`, `customLayout`, dll. tetap sama; yang berganti hanya dependency internal (support → AndroidX).
- Jika belum bisa pakai AndroidX, pin versi lama: `com.github.mafmudin:MPopUp:v1`.

### cara menambahkan MPopUp ke project android studio dengan menggunakan gradle
*  tambahkan kode di bawah kedalam file ```build.gradle``` (root project)
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
  
* lalu, tambahkan dependesi berikut kedalam file ```build.gradle``` (module aplikasi)
```
dependencies {
	 implementation 'com.github.mafmudin:MPopUp:1.0.0'
}
```

### cara menambahkan MPopUp ke project android studio dengan menggunakan maven
* tambahkan *jitpack repository* ke dalam build file maven

```
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

* tambahkan dependesinya

```
<dependency>
	<groupId>com.github.mafmudin</groupId>
	<artifactId>MPopUp</artifactId>
	<version>1.0.0</version>
</dependency>
```

## cara menggunakan MPopUp

### dengan menggunakan custom layout yang di buat sendiri
```
final MPopUp mPopUp = new MPopUp();
//inisiasi objek MPopUp

final View customview = mPopUp.customLayout(R.layout.popup_dialog, MainActivity.this);
//buatlah variabel view dan panggil fungsi customLayout dari class MPopUp yang akan mengembalikan view berdasarkan layout yang dibuat dengan tampilan dialog
```

* kemudian gunakan komponen yang ada pada layout

```
Button ok = customview.findViewById(R.id.btn_ok);
Button cancel = customview.findViewById(R.id.btn_cancel);

ok.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View view) {
		mPopUp.dismiss();
		Toast.makeText(MainActivity.this, "Di klik dengan custom view", Toast.LENGTH_SHORT).show();
	}
});

cancel.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View view) {
		mPopUp.dismiss();
	}
});
```

### dengan menggunakan layout builder 

```
final MPopUp mPopUp = new MPopUp();
//inisiai objek MPopUp
mPopUp.in(MainActivity.this)
                        .setTitle("Title")
                        .setMessage("Pesan")
                        .setBtPositiveBg(R.drawable.button_primary_round)
                        .setBtNegativeBg(R.drawable.button_primary_round_white)
                        .setBtPositiveTextColor(getResources().getColor(R.color.white))
                        .setBtNegativeTextColor(getResources().getColor(R.color.primary))
                        .setBgColor(R.color.primary)
                        .setRadius(20.0f)
                        .show();
//configurasi tampilan popUp dengan memanggil beberapa fungsi
```
* ```setTitle(String title)``` , digunakan untuk mengatur judul pop up
* ```setMessage(String message)```, digunakan untuk membuat pesan pada pop up
* ```setBtPositiveBg(int buttonPositiveBackground)```, digunakan untuk mengubah background tombol positive (diisi dengan drawable)
* ```setBtNegativeBg(int buttonNegativeBackground)```, digunakan untuk mengubah background tombol negative (diisi dengan drawable)
* ```setBtPositiveTextColor(int color)```, digunakan untuk mengubah tombol positive (diisi dengan resource color)
* ```setBtNegativeTextColor(int color)```, digunakan untuk mengubah tombol negative (diisi dengan resource color)
* ```setPositiveButton(String positiveButton)```, digunakan untuk mengubah text pada tombol positive
* ```setNegativeButton(String negativeButton)```, digunakan untuk mengubah text pada tombol negative
* ```setBgColor(int color)```, digunakan untuk mengubah warna background pop up
* ```setRadius(folat radius)```, di gunakan untuk mengubah tepi pop up rounded atau persegi
* ```show()```, di gunakan untuk menampilkan pop up

```
```
                mPopUp.setOnOkClickListener(new MPopUp.OnClick() {
                    @Override
                    public void onClick() {
                        mPopUp.dismiss();
                        Toast.makeText(MainActivity.this, "Di klik tanpa custom", Toast.LENGTH_SHORT).show();
                    }
                });
//listener yang dapat digunakan untuk melakukan action pada tombol ok (positive)

                mPopUp.setOnCancleClickListener(new MPopUp.OnClick() {
                    @Override
                    public void onClick() {
                        mPopUp.dismiss();
                    }
                });
////listener yang dapat digunakan untuk melakukan action pada tombol cancle (negative)
								
```
