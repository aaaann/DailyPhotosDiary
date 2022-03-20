package com.annevonwolffen.lintchecks

object TestRecyclerViewMissingAttributesDetectorDataProvider {

    fun getLayoutFileWithRecyclerViewWithoutOverScrollMode(): String {
        return """
            <?xml version="1.0" encoding="utf-8"?>
            <androidx.cardview.widget.CardView xmlns:android="http://schemas.android.com/apk/res/android"
                xmlns:app="http://schemas.android.com/apk/res-auto"
                xmlns:tools="http://schemas.android.com/tools"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginVertical="8dp"
                android:orientation="vertical"
                app:cardBackgroundColor="@color/gray_300"
                app:cardElevation="8dp">

                    <androidx.recyclerview.widget.RecyclerView
                        android:id="@+id/rv_images"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:clipToPadding="false"
                        android:orientation="horizontal"
                        android:paddingHorizontal="8dp"
                        app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager" />

            </androidx.cardview.widget.CardView>
        """.trimIndent()
    }

    fun getLayoutFileWithRecyclerViewWithOverScrollModeAlways(): String {
        return """
            <?xml version="1.0" encoding="utf-8"?>
            <androidx.cardview.widget.CardView xmlns:android="http://schemas.android.com/apk/res/android"
                xmlns:app="http://schemas.android.com/apk/res-auto"
                xmlns:tools="http://schemas.android.com/tools"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginVertical="8dp"
                android:orientation="vertical"
                app:cardBackgroundColor="@color/gray_300"
                app:cardElevation="8dp">

                    <androidx.recyclerview.widget.RecyclerView
                        android:id="@+id/rv_images"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:clipToPadding="false"
                        android:orientation="horizontal"
                        android:paddingHorizontal="8dp"
                        android:overScrollMode="always"
                        app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager" />

            </androidx.cardview.widget.CardView>
        """.trimIndent()
    }

    fun getLayoutFileWithRecyclerViewWithOverScrollModeNever(): String {
        return """
            <?xml version="1.0" encoding="utf-8"?>
            <androidx.cardview.widget.CardView xmlns:android="http://schemas.android.com/apk/res/android"
                xmlns:app="http://schemas.android.com/apk/res-auto"
                xmlns:tools="http://schemas.android.com/tools"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginVertical="8dp"
                android:orientation="vertical"
                app:cardBackgroundColor="@color/gray_300"
                app:cardElevation="8dp">

                    <androidx.recyclerview.widget.RecyclerView
                        android:id="@+id/rv_images"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:clipToPadding="false"
                        android:orientation="horizontal"
                        android:paddingHorizontal="8dp"
                        android:overScrollMode="never"
                        app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager" />

            </androidx.cardview.widget.CardView>
        """.trimIndent()
    }

    fun getErrorMessage() {

    }
}