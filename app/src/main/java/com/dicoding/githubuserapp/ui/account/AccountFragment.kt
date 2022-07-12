package com.dicoding.githubuserapp.ui.account

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import com.dicoding.githubuserapp.R
import com.dicoding.githubuserapp.databinding.FragmentAccountBinding
import com.dicoding.githubuserapp.notifreceiver.MyReceiver

class AccountFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentAccountBinding? = null
    private lateinit var notifReceiver: MyReceiver

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding as FragmentAccountBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notifReceiver = MyReceiver()
        setHasOptionsMenu(true)

        binding.btnAktif.setOnClickListener(this)
        binding.btnNonaktif.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_setting, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bahasa -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_aktif -> {
                notifReceiver.setNotification(context,
                    "Reminder", "Sudah saatnya untuk kembali membuka aplikasi")
            }
            R.id.btn_nonaktif -> {
                notifReceiver.cancelNotification(context)
            }
        }
    }
}