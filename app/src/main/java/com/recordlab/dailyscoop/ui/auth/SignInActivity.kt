package com.recordlab.dailyscoop.ui.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.auth.model.Prompt
import com.kakao.sdk.user.UserApiClient
import com.recordlab.dailyscoop.MainActivity
import com.recordlab.dailyscoop.R
import com.recordlab.dailyscoop.databinding.ActivitySignInBinding
import com.recordlab.dailyscoop.network.RetrofitClient.service
import com.recordlab.dailyscoop.network.enqueue
import com.recordlab.dailyscoop.network.request.RequestSignIn
import kotlinx.coroutines.launch


class SignInActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    val GOOGLE_ACCOUNT = "GOOGLE"
    private val TAG = "SIGN_IN_ACTIVITY"
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onStart() {
        super.onStart()
        //기존 로그인 사용자 확인. 이미 로그인한 구글 계정이 있으면 non-null
        var account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(applicationContext)
        val currentUser = mAuth.currentUser
        // 구글 로그인 된 상태면
        if (currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.putExtra(GOOGLE_ACCOUNT, account)
            startActivity(intent)
            finish()
            Log.d(TAG, "on Start works successful")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //Build a GoogleSignInClient w/ the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        sharedPref = getSharedPreferences("TOKEN", MODE_PRIVATE)
        editor = sharedPref.edit()


        // 가입하기 버튼 클릭
        val signupBtnClicked = binding.signYet2
        signupBtnClicked.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        // 로그인 버튼 클릭
        val loginBtnClicked = binding.loginButton
        loginBtnClicked.setOnClickListener {
            //Toast.makeText(this.getApplicationContext(),"로그인", Toast.LENGTH_SHORT).show();
            val email = binding.loginEmailText.text.toString()
            val password = binding.loginPasswordText.text.toString()

            val data = RequestSignIn(email, password)
            signIn(data)

            val pref = getSharedPreferences("TOKEN", 0)
            val edit = pref.edit() // 수정모드(추가, 수정)
            edit.putString("email", email) // key, value
            edit.apply() // 저장 완료
        }

        // 비밀번호 찾기 버튼 클릭
        val findPasswordBtnClicked = binding.loginPasswordButton
        findPasswordBtnClicked.setOnClickListener {
            //Toast.makeText(this.getApplicationContext(),"비밀번호 찾기", Toast.LENGTH_SHORT).show();
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        // 카카오로그인 버튼 클릭
        val socialKBtnClicked = binding.kakaoBtn
        socialKBtnClicked.setOnClickListener {
            UserApiClient.instance.loginWithKakaoAccount(
                this,
                prompts = listOf(Prompt.LOGIN)
            ) { token, error ->
                if (error != null) {
                    Toast.makeText(this.applicationContext, "다시 시도해 주세요", Toast.LENGTH_SHORT)
                        .show();
                } else if (token != null) {
                    Log.i("kakao", "로그인 성공 ${token.accessToken}")

                    // 카카오 토큰 서버로 전송
                    val header = mutableMapOf<String, String?>()
                    header["Authorization"] = token.accessToken
                    service.requestSocial(header = header, type = "kakao", token = "").enqueue(
                        onSuccess = {
                            when (it.code()) {
                                in 200..209 -> {
                                    val nic = it.body()?.nickname
                                    val to = it.body()?.token
                                    Toast.makeText(
                                        this.applicationContext,
                                        "반갑습니다",
                                        Toast.LENGTH_SHORT
                                    ).show();

                                    val pref = getSharedPreferences("TOKEN", 0)
                                    val edit = pref.edit() // 수정모드(추가, 수정)
                                    edit.putString("token", "Bearer ".plus(to)) // key, value
                                    edit.putString("nickname", nic)
                                    edit.putString("social", "true")
                                    edit.apply() // 저장 완료

                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                else -> {
                                    Toast.makeText(
                                        this.applicationContext,
                                        "다시 시도해 주세요",
                                        Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                        }
                    )

//                    // 사용자 정보 요청 (기본)
//                    UserApiClient.instance.me { user, errorr ->
//                        if (errorr != null) {
//                            Log.e("kakao", "사용자 정보 요청 실패", errorr)
//                        }
//                        else if (user != null) {
//                            Log.i("kakao", "사용자 정보 요청 성공" +
//                                    "\n회원번호: ${user.id}" +
//                                    "\n이메일: ${user.kakaoAccount?.email}" +
//                                    "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
//                                    "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
//                        }
//                    }
                }
            }
        }

        // 구글 로그인 버튼 클릭
        val googleBtnClicked = binding.btnGoogle
        googleBtnClicked.setOnClickListener(this)
    }

    private fun signIn(data: RequestSignIn) {
        service.requestSignIn(body = data).enqueue(
            onSuccess = {
                when (it.code()) {
                    in 200..209 -> {
                        val nic = it.body()?.nickname
                        val to = it.body()?.token
                        Toast.makeText(this.applicationContext, "반갑습니다", Toast.LENGTH_SHORT).show();

                        val pref = getSharedPreferences("TOKEN", 0)
                        val edit = pref.edit() // 수정모드(추가, 수정)
                        edit.putString("token", "Bearer ".plus(to)) // key, value
                        edit.putString("nickname", nic)
                        edit.apply() // 저장 완료

                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    401 -> {
                        Toast.makeText(
                            this.applicationContext,
                            "아이디나 비밀번호를 확인해주세요",
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
        )
    }

    private fun signIn() {
        Log.d(TAG, "signIn 도착")
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN)
    }

    companion object {
        private val REQUEST_CODE_SIGN_IN = 9000
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
//            handleSignInResult(task);
            val result: GoogleSignInResult? = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result!!.isSuccess) {
                val account: GoogleSignInAccount? = result.signInAccount
                Log.d(TAG, "어카운트로 받아오기. -> ${account?.idToken}")
            }
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "이름 = " + account.displayName)
                Log.d(TAG, "이메일 = " + account.email)
                Log.d(TAG, "getID() = " + account.id)
                Log.d(TAG, "getAccount() = " + account.account)
                Log.d(TAG, "getIDToken() = " + account.idToken)

                firebaseAuthWithGoogle(account.idToken!!, task)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this, "로그인에 실패하셨습니다.", Toast.LENGTH_SHORT).show()
                // ...
            }
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            Log.d(TAG, "이름 = " + account.displayName)
            Log.d(TAG, "이메일 = " + account.email)
            Log.d(TAG, "getID() = " + account.id)
            Log.d(TAG, "getAccount() = " + account.account)
            Log.d(TAG, "getIDToken() = " + account.idToken)
            // Signed in successfully, show authenticated UI.
            val idToken = account.idToken
            // server 토큰 전송은
            Log.d(TAG, "핸들 사인 인 결과 -> $idToken")

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
//            updateUI(null)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, completeTask: Task<GoogleSignInAccount>) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
//                    val user = mAuth.currentUser
                    val user: GoogleSignInAccount? =
                        completeTask.getResult(ApiException::class.java)
//                    val authCode : String? = user?.serverAuthCode
//                    val account: GoogleSignInAccount? = result.signInAccount
//                    Log.d(TAG, "authCode: $authCode")
                    val header = mutableMapOf<String, String>()
                    if (user?.idToken != null) {
                        header["Authorization"] = user.idToken
                        lifecycleScope.launch {
                            service.requestSocial(header = header, type = "google", token = idToken).enqueue(
                                onSuccess = { result ->
                                    when (result.code()) {
                                        in 200..206 -> {
                                            val nickname = result.body()?.nickname
                                            val jwt = result.body()?.token

                                            editor.putString("nickname", nickname)
                                            editor.putString("token", "Bearer $jwt")
                                            editor.apply()
                                            editor.commit()
                                            goMain()
                                        }
                                    }
                                }, onError = {

                                }
                            )

                        }
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    val snackbar = Snackbar.make(
                        binding.root,
                        "로그인에 실패하였습니다.\n확인을 누르면 사라집니다.",
                        Snackbar.LENGTH_SHORT
                    )
                    snackbar.setAction("확인", View.OnClickListener {
                        snackbar.dismiss()
                    })
                    snackbar.show()
                }
            }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_google -> {
                signIn()
            }
        }
    }

    fun goMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}