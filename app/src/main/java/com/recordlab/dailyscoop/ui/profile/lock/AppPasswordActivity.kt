package com.recordlab.dailyscoop.ui.profile.lock

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivityAppLockPasswordBinding

class AppPasswordActivity : AppCompatActivity() {
    private var oldPwd = ""
    private var changePwdUnlock = false
    private var _binding: ActivityAppLockPasswordBinding? = null
    private val binding get() = _binding!!
    private var num1 = ""
    private var num2 = ""
    private var num3 = ""
    private var num4 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAppLockPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val buttonArray = arrayListOf(
            binding.btnLockNum0,
            binding.btnLockNum1,
            binding.btnLockNum2,
            binding.btnLockNum3,
            binding.btnLockNum4,
            binding.btnLockNum5,
            binding.btnLockNum6,
            binding.btnLockNum7,
            binding.btnLockNum8,
            binding.btnLockNum9,
            binding.btnLockClear,
            binding.btnLockDel
        )

        val editTextArray = arrayListOf(
            binding.etPasscode1,
            binding.etPasscode2,
            binding.etPasscode3,
            binding.etPasscode4
        )

        for (button in buttonArray) {
            button.setOnClickListener(btnListener)
        }

        for (e_text in editTextArray) {
            e_text.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                    return true
                }
            })
        }

        binding.tvLockContent.text = "비밀번호를 입력해 주세요"
        val t = intent.getIntExtra("type", 0)
        when (t) {
            AppLockConst.ENABLE_PASSLOCK -> {
                binding.tvLockTitle.text = "잠금 설정"
            }
            AppLockConst.CHANGE_PASSWORD -> {
                binding.tvLockTitle.text = "비밀번호 변경"
            }
            AppLockConst.DISABLE_PASSLOCK -> {
                binding.tvLockTitle.text = "잠금 해제"
            }
            AppLockConst.UNLOCK_PASSWORD -> {
                binding.tvLockTitle.text = "비밀번호"
            }
        }
    }

    // 버튼 클릭 했을 때
    private val btnListener = View.OnClickListener { view ->
        var currentValue = -1
        when (view.id) {
            R.id.btn_lock_num0 -> currentValue = 0
            R.id.btn_lock_num1 -> currentValue = 1
            R.id.btn_lock_num2 -> currentValue = 2
            R.id.btn_lock_num3 -> currentValue = 3
            R.id.btn_lock_num4 -> currentValue = 4
            R.id.btn_lock_num5 -> currentValue = 5
            R.id.btn_lock_num6 -> currentValue = 6
            R.id.btn_lock_num7 -> currentValue = 7
            R.id.btn_lock_num8 -> currentValue = 8
            R.id.btn_lock_num9 -> currentValue = 9
            R.id.btn_lock_clear -> onClear()
            R.id.btn_lock_del -> onDeleteKey()
        }

        val strCurrentValue = currentValue.toString()
        if (currentValue != -1) {
            when {
                binding.etPasscode1.isFocused -> {
                    num1 = strCurrentValue
                    binding.etPasscode1.setText("●")
                    binding.etPasscode2.setText("")
                    binding.etPasscode2.requestFocus()
                }
                binding.etPasscode2.isFocused -> {
                    num2 = strCurrentValue
                    binding.etPasscode2.setText("●")
                    binding.etPasscode3.setText("")
                    binding.etPasscode3.requestFocus()
                }
                binding.etPasscode3.isFocused -> {
                    num3 = strCurrentValue
                    binding.etPasscode3.setText("●")
                    binding.etPasscode4.setText("")
                    binding.etPasscode4.requestFocus()
                }
                binding.etPasscode4.isFocused -> {
                    num4 = strCurrentValue
                    binding.etPasscode4.setText("●")
                }
            }
        }

        // 비밀번호를 4자리 모두 입력 시
        if (binding.etPasscode4.text.isNotEmpty() && binding.etPasscode3.text.isNotEmpty() && binding.etPasscode2.text.isNotEmpty() && binding.etPasscode1.text.isNotEmpty()) {
            inputType(intent.getIntExtra("type", 0))
        }
    }

    // 한 칸 지우기를 눌렀을 때
    private fun onDeleteKey() {
        when {
            binding.etPasscode1.isFocused -> {
                binding.etPasscode1.setText("")
                num1 = ""
            }
            binding.etPasscode2.isFocused -> {
                binding.etPasscode1.setText("")
                binding.etPasscode1.requestFocus()
                num1 = ""
            }
            binding.etPasscode3.isFocused -> {
                binding.etPasscode2.setText("")
                binding.etPasscode2.requestFocus()
                num2 = ""
            }
            binding.etPasscode4.isFocused -> {
                binding.etPasscode3.setText("")
                binding.etPasscode3.requestFocus()
                num3 = ""
            }
        }
    }

    // 모두 지우기
    private fun onClear() {
        binding.etPasscode1.setText("")
        binding.etPasscode2.setText("")
        binding.etPasscode3.setText("")
        binding.etPasscode4.setText("")
        num1 = ""
        num2 = ""
        num3 = ""
        num4 = ""
        binding.etPasscode1.requestFocus()
    }

    // 입력된 비밀번호 합치기
    private fun inputedPassword() : String {
        return "${num1}${num2}${num3}${num4}"
    }

    // intent type 분류
    private fun inputType(type : Int) {
        when (type) {
            AppLockConst.ENABLE_PASSLOCK -> { // 잠금설정
                if (oldPwd.isEmpty()) {
                    oldPwd = inputedPassword()
                    onClear()
                    binding.tvLockContent.text = "비밀번호를 다시 한번 입력해 주세요"
                } else {
                    if (oldPwd == inputedPassword()) {
                        AppLock(this).setPassLock(inputedPassword())
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        onClear()
                        oldPwd = ""
                        binding.tvLockContent.text = "비밀번호가 일치하지 않습니다"
                    }
                }
            }

            AppLockConst.DISABLE_PASSLOCK -> { // 잠금삭제
                if (AppLock(this).isPassLockSet()) {
                    if (AppLock(this).checkPassLock(inputedPassword())) {
                        AppLock(this).removePassLock()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        binding.tvLockContent.text = "비밀번호를 다시 확인해 주십시오"
                        onClear()
                    }
                } else {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            }

            AppLockConst.UNLOCK_PASSWORD -> {
                if (AppLock(this).checkPassLock(inputedPassword())) {
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    binding.tvLockContent.text = "비밀번호를 다시 확인해 주십시오"
                    onClear()
                }
            }

            AppLockConst.CHANGE_PASSWORD -> { // 비밀번호 변경
                if (AppLock(this).checkPassLock(inputedPassword()) && !changePwdUnlock) {
                    onClear()
                    changePwdUnlock = true
                    binding.tvLockContent.text = "새로운 비밀번호를 입력해 주세요"
                } else if (changePwdUnlock) {
                    if (oldPwd.isEmpty()) {
                        oldPwd = inputedPassword()
                        onClear()
                        binding.tvLockContent.text = "비밀번호 다시 한번 입력해 주세요"
                    } else {
                        if (oldPwd == inputedPassword()) {
                            AppLock(this).setPassLock(inputedPassword())
                            setResult((Activity.RESULT_OK))
                            finish()
                        } else {
                            onClear()
                            oldPwd = ""
                            binding.tvLockContent.text = "새로운 비밀번호를 다시 입력해 주세요"
                            changePwdUnlock = false
                        }
                    }
                } else {
                    binding.tvLockContent.text = "비밀번호를 다시 확인해 주십시오"
                    changePwdUnlock = false
                    onClear()
                }
            }
        }
    }

}