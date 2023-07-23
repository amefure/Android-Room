package com.example.room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.room.room.User
import com.example.room.room.UserDao
import com.example.room.room.UserDatabase
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var db : UserDatabase
        lateinit var dao : UserDao
    }
    private val compositeDisposable = CompositeDisposable()
    private lateinit var adapter: MainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val display:TextView = findViewById(R.id.display_text)
        val addButton:Button = findViewById(R.id.add_button)
        val getButton:Button = findViewById(R.id.get_button)
        val allDeleteButton:Button = findViewById(R.id.all_delete_button)
        val input:EditText = findViewById(R.id.input_text)

        db = UserDatabase.getDatabase(this)
        dao = db.userDao()

        addButton.setOnClickListener{
            if (input.text.toString() != "") {
                display.text = "入力したよ"
                insUser(input.text.toString())
            }else{
                display.text = "未入力です"
            }
        }

        getButton.setOnClickListener {
            display.text = "取得したよ"
            getUser(1)
        }

        allDeleteButton.setOnClickListener {
            display.text = "リセットしました"
            deleteAll()
        }

        subscribeAllUser()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    private fun insUser(name: String) {
        val user = User(0, name, 20, "サーフィン")

        Completable.fromAction {
            dao.insert(user)
        }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    private fun getUser(id : Int){
        val display: TextView = findViewById(R.id.display_text)

        val userLiveData = dao.getLiveDataUser(id)
        userLiveData.observe(this, { user ->
            if (user != null) {
                display.text = user.name
            } else {
                display.text = "User not found"
            }
        })
    }

    private fun deleteAll(){
        Completable.fromAction { dao.deleteAll() }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    private fun subscribeAllUser(){

        val recyclerView: RecyclerView = findViewById(R.id.main_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        )

        compositeDisposable.add(
            dao.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        //データ取得完了時の処理
                        val data = ArrayList<User>()
                        it.forEach {
                                user -> data.add(user)
                        }
                        val adapter = MainAdapter(data)
                        recyclerView.adapter = adapter
                    }
                )
        )
    }


}
