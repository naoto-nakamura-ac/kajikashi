package com.example.kajikashiApp

import com.example.kajikashiApp.dto.LoginRequest
import com.example.kajikashiApp.dto.RegisterRequest
import com.example.kajikashiApp.dto.RegisterResponse
import com.example.kajikashiApp.repository.FamilyRepository
import com.example.kajikashiApp.repository.SessionRepository
import com.example.kajikashiApp.repository.UserRepository
import org.assertj.core.util.Sets.set
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.test.context.ActiveProfiles
import java.lang.reflect.Array.set

@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KajikashiAppApplicationTests(
	@Autowired val restTemplate: TestRestTemplate,
	@LocalServerPort val port: Int
) {
	@Autowired
	private lateinit var userRepository: UserRepository

	@Autowired
	private lateinit var sessionRepository: SessionRepository

	@Autowired
	private lateinit var familyRepository: FamilyRepository

	@BeforeEach
	fun setup() {
		// 各テストは項目が空の状態で始める。
		sessionRepository.deleteAll()
		userRepository.deleteAll()
		familyRepository.deleteAll()
	}
	@Test
	fun contextLoads() {
	}

	@Test
	fun `GETリクエストはOKステータスを返す`() {
		// 初期セット（アカウント作成→ログイン→ヘッダにセッショントークンつける）
		val request = RegisterRequest("John", "hoge@hoge.com", "pass", "null")
		val responseRegister = restTemplate.postForEntity("http://localhost:$port/api/auth/register",request,
			RegisterResponse::class.java)
		val token = responseRegister.body?.token
		println(token)
		val headers = HttpHeaders().apply{
			set("Authorization", "Bearer $token")
		}
		val entity = HttpEntity<String>(headers)
		// ↑ 初期セット　↓exchangeを使う
		val response = restTemplate.exchange("http://localhost:$port/api/auth", HttpMethod.GET,entity, String::class.java)
		assertThat(response.statusCode, equalTo(HttpStatus.OK))
		assertThat(response.body, equalTo("OK"))
	}

	@Test
	fun `ユーザー登録ができる`(){
		val request = RegisterRequest("John", "hoge@hoge.com", "pass", "null")
		val response = restTemplate.postForEntity("http://localhost:$port/api/auth/register",request, String::class.java)
		assertThat(response.statusCode, equalTo(HttpStatus.CREATED))
	}

	@Test
	fun `同名のアドレスがあればBadRequestになる`(){
		val request = RegisterRequest("John", "hoge@hoge.com", "pass", "null")
		restTemplate.postForEntity("http://localhost:$port/api/auth/register",request, String::class.java)
		val response = restTemplate.postForEntity("http://localhost:$port/api/auth/register",request, String::class.java)
		assertThat(response.statusCode, equalTo(HttpStatus.BAD_REQUEST))
	}

	@Test
	fun `ログインできる`(){
		val requestRegister = RegisterRequest("John", "hoge@hoge.com", "pass", "null")
		restTemplate.postForEntity("http://localhost:$port/api/auth/register",requestRegister, String::class.java)
		val request = LoginRequest("hoge@hoge.com",  "pass")
		val response = restTemplate.postForEntity("http://localhost:$port/api/auth/login",request, String::class.java)
		assertThat(response.statusCode, equalTo(HttpStatus.OK))
	}
	@Test
	fun `パスワードが違う場合はエラー`(){
		val requestRegister = RegisterRequest("John", "hoge@hoge.com", "pass", "null")
		restTemplate.postForEntity("http://localhost:$port/api/auth/register",requestRegister, String::class.java)
		val request = LoginRequest("hoge@hoge.com",  "word")
		val response = restTemplate.postForEntity("http://localhost:$port/api/auth/login",request, String::class.java)
		assertThat(response.statusCode, equalTo(HttpStatus.UNAUTHORIZED))
	}
	@Test
	fun `存在しないユーザーの場合はエラー`(){
		val requestRegister = RegisterRequest("John", "hoge@hoge.com", "pass", "null")
		restTemplate.postForEntity("http://localhost:$port/api/auth/register",requestRegister, String::class.java)
		val request = LoginRequest("fuga@hoge.com",  "pass")
		val response = restTemplate.postForEntity("http://localhost:$port/api/auth/login",request, String::class.java)
		assertThat(response.statusCode, equalTo(HttpStatus.UNAUTHORIZED))
	}
	@Test
	fun `ログアウトできる`(){
		val request = RegisterRequest("John", "hoge@hoge.com", "pass", "null")
		val responseRegister = restTemplate.postForEntity("http://localhost:$port/api/auth/register",request,
			RegisterResponse::class.java)
		val token = responseRegister.body?.token
		val headers = HttpHeaders().apply{
			set("Authorization", "Bearer $token")
		}
		val entity = HttpEntity<Void>(headers)
		val response = restTemplate.exchange("http://localhost:$port/api/auth/logout", HttpMethod.DELETE,entity, String::class.java)
		assertThat(response.statusCode, equalTo(HttpStatus.OK))
	}

}
