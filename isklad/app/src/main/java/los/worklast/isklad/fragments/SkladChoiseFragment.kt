package los.worklast.isklad.fragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import androidx.fragment.app.commit
import layout.Prihod_yxod_choise
import los.worklast.isklad.R

class SkladChoiseFragment : Fragment(R.layout.sklad_choise) {


       override fun onViewCreated(view: View, savedInstanceState: Bundle?) {



              val buttonSklad1 = view.findViewById<Button>(R.id.one_sklad).setOnClickListener {


                  val bundle = bundleOf("sklad" to 1)
                  parentFragmentManager.commit {
                      setReorderingAllowed(true)
                      add<Prihod_yxod_choise>(R.id.main_fragment_container, args = bundle)
                  }
//

              }

            val buttonSklad2 = view.findViewById<Button>(R.id.two_sklad).setOnClickListener {

                 val bundle = bundleOf("sklad" to 2)
                 parentFragmentManager.commit {
                      setReorderingAllowed(true)
                      add<Prihod_yxod_choise>(R.id.main_fragment_container, args = bundle)
                  }

              }


            val buttonSklad3 = view.findViewById<Button>(R.id.three_sklad).setOnClickListener {

                    val bundle = bundleOf("sklad" to 3)
                 parentFragmentManager.commit {
                      setReorderingAllowed(true)
                      add<Prihod_yxod_choise>(R.id.main_fragment_container, args = bundle)
                  }
              }

       // val someInt = requireArguments().getInt("some_int")

    }


}