package com.dicoding.githubuserapp.detailuser.activity

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuserapp.db.DatabaseContract
import com.dicoding.githubuserapp.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.databinding.ActivityDetailUserBinding
import com.dicoding.githubuserapp.detailuser.adapter.SectionPagerAdapter
import com.dicoding.githubuserapp.objectparcelable.User
import com.dicoding.githubuserapp.helper.MappingHelper
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*

@DelicateCoroutinesApi
class DetailUserActivity : AppCompatActivity(), View.OnClickListener {

    private var isDelete = false
    private var click2Kali = false
    private var dataUserFav: User? = null
    private var dataUser: User? = null
    private var position: Int = 0
    private var data = arrayListOf<String>()
    private var dataCheck = arrayListOf<String>()
    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataUserFav = intent.getParcelableExtra(EXTRA_FAVORITE)
        if (dataUserFav != null) {
            detailUser(dataUserFav)
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isDelete = true
        } else {
            dataUser = intent.getParcelableExtra(EXTRA_DATA)
            detailUser(dataUser)
        }

        checkData(dataUser?.username)

        if (isDelete) {
            uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + dataUserFav?.id)

            val cursor = contentResolver.query(uriWithId, null, null,
                null, null)

            if (cursor != null) {
                dataUserFav = MappingHelper.mapCursorToObject(cursor)
                cursor.close()
            }
        }

        binding.btnFollow.setOnClickListener(this)
        binding.btnUnfollow.setOnClickListener(this)
        binding.btnFavorite.setOnClickListener(this)

        val sectionPagerAdapter = SectionPagerAdapter(this)
        sectionPagerAdapter.data = data
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun checkData(username: String?) {
        GlobalScope.launch(Dispatchers.Main) {
            val data = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null,
                    null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val dataFavorite = data.await()

            for (i in dataFavorite.indices) {
                dataCheck.add(dataFavorite[i].username.toString())
            }

            if (username in dataCheck) {
                click2Kali = true
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isDelete) {
            menuInflater.inflate(R.menu.menu_delete, menu)
            Log.d("isEdit", isDelete.toString())
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (isDelete) {
            when (item.itemId) {
                R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
                android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
            }
        } else {
            when (item.itemId) {
                android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_follow -> {
                val addFollowers = data[0].toInt() + 1
                binding.tvFollowers.text = addFollowers.toString()
                binding.btnFollow.visibility = View.GONE
                binding.btnUnfollow.visibility = View.VISIBLE
            }
            R.id.btn_unfollow -> {
                binding.tvFollowers.text = data[0]
                binding.btnFollow.visibility = View.VISIBLE
                binding.btnUnfollow.visibility = View.GONE
            }
            R.id.btn_favorite -> {
                if (isDelete || click2Kali) {
                    Toast.makeText(this, "Data sudah ditambahkan difavorit",
                        Toast.LENGTH_SHORT).show()
                } else {
                    click2Kali = true

                    val values = ContentValues()
                    values.put(DatabaseContract.FavoriteColumns.NAME, data[3])
                    values.put(DatabaseContract.FavoriteColumns.USERNAME, data[2])
                    values.put(DatabaseContract.FavoriteColumns.AVATAR, data[7])
                    values.put(DatabaseContract.FavoriteColumns.COMPANY, data[4])
                    values.put(DatabaseContract.FavoriteColumns.LOCATION, data[5])
                    values.put(DatabaseContract.FavoriteColumns.REPOSITORY, data[6])
                    values.put(DatabaseContract.FavoriteColumns.FOLLOWERS, data[0])
                    values.put(DatabaseContract.FavoriteColumns.FOLLOWING, data[1])

                    contentResolver.insert(CONTENT_URI, values)

                    Toast.makeText(this, "Berhasil ditambahkan ke favorit",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun detailUser(dataUser: User?) {
        data.add(dataUser?.followers.toString())
        data.add(dataUser?.following.toString())
        data.add(dataUser?.username.toString())
        data.add(dataUser?.name.toString())
        data.add(dataUser?.company.toString())
        data.add(dataUser?.location.toString())
        data.add(dataUser?.repository.toString())
        data.add(dataUser?.avatar.toString())


        supportActionBar?.title = dataUser?.username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailUserViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailUserViewModel::class.java)

        detailUserViewModel.setDetailUser(dataUser?.username.toString())

        detailUserViewModel.getDetailUsers().observe(this, {
                userItems -> if (userItems != null) {
            Glide.with(this)
                .load(userItems.avatar)
                .into(binding.imgAvatarReceived)

            with(binding) {
                tvNameReceived.text = userItems.name
                tvCompanyReceived.text = userItems.company
                tvLocationReceived.text = userItems.location
                tvRepository.text = userItems.repository.toString()
                tvFollowing.text = userItems.following.toString()
                tvFollowers.text = userItems.followers.toString()
            }
        }
        })
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE

        if (isDialogClose) {
            finish()
        } else {
            val dialogMessage = "Apakah anda yakin ingin menghapus item ini?"
            val dialogTitle = "Hapus Note"

            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle(dialogTitle)
            alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya") { _, _ ->
                    contentResolver.delete(uriWithId, null, null)
                    Toast.makeText(this@DetailUserActivity, "Data berhasil dihapus",
                        Toast.LENGTH_SHORT).show()
                    isDelete = false
                }
                .setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    companion object {
        const val EXTRA_DATA = "data"
        const val EXTRA_FAVORITE = "extra_favorite"
        const val EXTRA_POSITION = "extra_position"
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.following,
            R.string.followers
        )
    }
}