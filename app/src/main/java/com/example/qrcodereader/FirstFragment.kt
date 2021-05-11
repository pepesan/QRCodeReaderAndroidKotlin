package com.example.qrcodereader

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.qrcodereader.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var scannerView: CodeScannerView

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }
    private var permissionsRequired
    = arrayOf(Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scannerView = view.findViewById<CodeScannerView>(R.id.scanner_view)
        requestAllPermissions()

        //lanzaLector()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val MY_PERMISSIONS = 1

    fun requestAllPermissions() {
        if (ActivityCompat.checkSelfPermission(requireContext(), permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(requireContext(), permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("app", "lvl 1")
            //When permission is not granted by user, show them message why this permission is needed.
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permissionsRequired[0])
                && ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permissionsRequired[1])
            ) {
                Toast.makeText(requireContext(), "Please grant permissions to use camera", Toast.LENGTH_LONG).show()
                Log.d("app", "lvl 2")
                //Give user option to still opt-in the permissions
                ActivityCompat.requestPermissions(requireActivity(),
                    permissionsRequired,
                    MY_PERMISSIONS)

            } else {
                Log.d("app", "lvl 3")
                // Show user dialog to grant permissions
                ActivityCompat.requestPermissions(requireActivity(),
                    permissionsRequired,
                    MY_PERMISSIONS)
            }
        } else if (ActivityCompat.checkSelfPermission(requireContext(), permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(requireContext(), permissionsRequired[1]) == PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("app", "lvl 4")
            //Go ahead with recording audio now
            Toast.makeText(requireContext(), "Permissions granted to use all", Toast.LENGTH_LONG).show()
            lanzaLector()
        }//If permission is granted, then go ahead recording audio
    }

    private fun lanzaLector() {
        /*
        codeScanner = CodeScanner(requireContext(), scannerView)
        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            activity?.runOnUiThread {
                Toast.makeText(requireContext(), "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            activity?.runOnUiThread {
                Toast.makeText(requireContext(), "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

         */
        codeScanner = CodeScanner(requireActivity(), scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            activity?.runOnUiThread {
                Toast.makeText(activity, it.text, Toast.LENGTH_LONG).show()
            }
        }
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    //Handling callback
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {

                Log.d("app", "on request: "+ grantResults)
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                        Log.d("app", "Permisos camara")
                    lanzaLector()
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(requireContext(), "Permissions Denied to use camera", Toast.LENGTH_LONG).show()
                }
                return
            }

}
