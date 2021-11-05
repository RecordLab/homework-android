package com.recordlab.dailyscoop.ui.profile.lock

import android.app.Activity
import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAppLockPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val buttonArray = arrayListOf<Button>(
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

        for (button in buttonArray) {
            button.setOnClickListener(btnListener)
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
                    setEditText(binding.etPasscode1, binding.etPasscode2, strCurrentValue)
                }
                binding.etPasscode2.isFocused -> {
                    setEditText(binding.etPasscode2, binding.etPasscode3, strCurrentValue)
                }
                binding.etPasscode3.isFocused -> {
                    setEditText(binding.etPasscode3, binding.etPasscode4, strCurrentValue)
                }
                binding.etPasscode4.isFocused -> {
                    binding.etPasscode4.setText(strCurrentValue)
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
            }
            binding.etPasscode2.isFocused -> {
                binding.etPasscode1.setText("")
                binding.etPasscode1.requestFocus()
            }
            binding.etPasscode3.isFocused -> {
                binding.etPasscode2.setText("")
                binding.etPasscode2.requestFocus()
            }
            binding.etPasscode4.isFocused -> {
                binding.etPasscode3.setText("")
                binding.etPasscode3.requestFocus()
            }
        }
    }

    // 모두 지우기
    private fun onClear() {
        binding.etPasscode1.setText("")
        binding.etPasscode2.setText("")
        binding.etPasscode3.setText("")
        binding.etPasscode4.setText("")
        binding.etPasscode1.requestFocus()
    }

    // 입력된 비밀번호 합치기
    private fun inputedPassword() : String {
        return "${binding.etPasscode1.text}${binding.etPasscode2.text}${binding.etPasscode3.text}${binding.etPasscode4.text}"
    }

    // EditText 설정
    private fun setEditText(currentEditText : EditText, nextEditText : EditText, strCurrentValue : String) {
        currentEditText.setText(strCurrentValue)
        nextEditText.requestFocus()
        nextEditText.setText("")
    }

    // intent type 분류
    private fun inputType(type : Int) {
        when (type) {
            AppLockConst.ENABLE_PASSLOCK -> { // 잠금설정
                if (oldPwd.isEmpty()) {
                    oldPwd = inputedPassword()
                    onClear()
                    binding.tvLockTitle.text = "다시 한번 입력"
                } else {
                    if (oldPwd == inputedPassword()) {
                        AppLock(this).setPassLock(inputedPassword())
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        onClear()
                        oldPwd = ""
                        binding.tvLockTitle.text = "비밀번호 입력"
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
                        binding.tvLockTitle.text = "비밀번호가 틀립니다."
                        onClear()
                    }
                } else {
                    setResult(Activity.RESULT_CANCELED)
                    finish()
                }
            }

            AppLockConst.UNLOCK_PASSWORD ->
                if (AppLock(this).checkPassLock(inputedPassword())) {
                    setResult(Activity.RESULT_OK)
                    finish()
                } else {
                    binding.tvLockTitle.text = "비밀번호가 틀립니다."
                    onClear()
                }

            AppLockConst.CHANGE_PASSWORD -> { // 비밀번호 변경
                if (AppLock(this).checkPassLock(inputedPassword()) && !changePwdUnlock) {
                    onClear()
                    changePwdUnlock = true
                    binding.tvLockTitle.text = "새로운 비밀번호 입력"
                } else if (changePwdUnlock) {
                    if (oldPwd.isEmpty()) {
                        oldPwd = inputedPassword()
                        onClear()
                        binding.tvLockTitle.text = "새로운 비밀번호 다시 입력"
                    } else {
                        if (oldPwd == inputedPassword()) {
                            AppLock(this).setPassLock(inputedPassword())
                            setResult((Activity.RESULT_OK))
                            finish()
                        } else {
                            onClear()
                            oldPwd = ""
                            binding.tvLockTitle.text = "현재 비밀번호 다시 입력"
                            changePwdUnlock = false
                        }
                    }
                } else {
                    binding.tvLockTitle.text = "비밀번호가 틀립니다."
                    changePwdUnlock = false
                    onClear()
                }
            }
        }
    }
}