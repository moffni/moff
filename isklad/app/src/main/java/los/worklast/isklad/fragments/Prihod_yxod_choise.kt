package layout

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import los.worklast.isklad.R
import los.worklast.isklad.fragments.CameraFrag2

class Prihod_yxod_choise : Fragment(R.layout.prihod_yxod_fragment) {

      override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val someInt = requireArguments().getInt("sklad")
          println(" my sklad number == ${someInt}")

          val prihod = view.findViewById<Button>(R.id.prihod).setOnClickListener {

                val bund2 = bundleOf("sklad" to someInt , "final" to 1)
               parentFragmentManager.commit {
                      setReorderingAllowed(true)
                      add<CameraFrag2>(R.id.main_fragment_container , args = bund2)
                  }

          }



           val exit = view.findViewById<Button>(R.id.exit).setOnClickListener {

                     val bund2 = bundleOf("sklad" to someInt, "final" to 2)
                parentFragmentManager.commit {
                      setReorderingAllowed(true)
                      add<CameraFrag2>(R.id.main_fragment_container, args = bund2)
                  }

          }


           val waitings = view.findViewById<Button>(R.id.wait).setOnClickListener {

                 val bund2 = bundleOf("sklad" to someInt, "final" to 3)
                parentFragmentManager.commit {
                      setReorderingAllowed(true)
                      add<CameraFrag2>(R.id.main_fragment_container , args = bund2)
                  }

          }

          // CameraFrag2


    }


}
