package com.example.kajikashiApp

import com.example.kajikashiApp.dto.LoginRequest
import com.example.kajikashiApp.dto.RegisterRequest
import com.example.kajikashiApp.dto.RegisterResponse
import com.example.kajikashiApp.dto.SummaryResponse
import com.example.kajikashiApp.dto.TaskLogResponse
import com.example.kajikashiApp.dto.TaskRegisterRequest
import com.example.kajikashiApp.repository.FamilyRepository
import com.example.kajikashiApp.repository.SessionRepository
import com.example.kajikashiApp.repository.TaskRepository
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
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import java.lang.reflect.Array.set

@ActiveProfiles("dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class KajikashiAppApplicationTests(
	@Autowired val restTemplate: TestRestTemplate,
	@Autowired val jdbcTemplate: JdbcTemplate,
	@LocalServerPort val port: Int
) {
	@Autowired
	private lateinit var taskRepository: TaskRepository

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
		taskRepository.deleteAll()
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
		val headers = HttpHeaders().apply{
			set("Authorization", "Bearer $token")
		}
		val entity = HttpEntity<String>(headers)
		// ↑ 初期セット　↓exchangeを使う
		val response = restTemplate.exchange("http://localhost:$port/api/auth/me", HttpMethod.GET,entity, String::class.java)
		assertThat(response.statusCode, equalTo(HttpStatus.OK))
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
	@Test
	fun `家事一覧が取得できる`(){
		val request = RegisterRequest("John", "hoge@hoge.com", "pass", "null")
		val responseRegister = restTemplate.postForEntity("http://localhost:$port/api/auth/register",request,
			RegisterResponse::class.java)
		val token = responseRegister.body?.token
		val headers = HttpHeaders().apply{
			set("Authorization", "Bearer $token")
		}
		val entity = HttpEntity<String>(headers)
		// ↑ 初期セット　↓exchangeを使う
		val response = restTemplate.exchange("http://localhost:$port/api/tasks", HttpMethod.GET,entity, String::class.java)
		assertThat(response.statusCode, equalTo(HttpStatus.OK))
	}

	@Test
	fun `家事実績が登録できる`(){
		val request = RegisterRequest("John", "hoge@hoge.com", "pass", "null")
		val responseRegister = restTemplate.postForEntity("http://localhost:$port/api/auth/register",request,
			RegisterResponse::class.java)
		val token = responseRegister.body?.token
		val headers = HttpHeaders().apply{
			set("Authorization", "Bearer $token")
		}
		val taskRequest = TaskRegisterRequest("買い出し","hoge@hoge.com")
		val entity = HttpEntity(taskRequest, headers)
		// ↑ 初期セット　↓exchangeを使う
		val response = restTemplate.exchange("http://localhost:$port/api/tasks", HttpMethod.POST,entity, String::class.java)
		assertThat(response.statusCode, equalTo(HttpStatus.CREATED))
	}
	@Test
	fun `リストにない家事はBadRequest`(){
		val request = RegisterRequest("John", "hoge@hoge.com", "pass", "null")
		val responseRegister = restTemplate.postForEntity("http://localhost:$port/api/auth/register",request,
			RegisterResponse::class.java)
		val token = responseRegister.body?.token
		val headers = HttpHeaders().apply{
			set("Authorization", "Bearer $token")
		}
		val taskRequest = TaskRegisterRequest("何かしらのミスたいぽ","hoge@hoge.com")
		val entity = HttpEntity(taskRequest, headers)
		// ↑ 初期セット　↓exchangeを使う
		val response = restTemplate.exchange("http://localhost:$port/api/tasks", HttpMethod.POST,entity, String::class.java)
		assertThat(response.statusCode, equalTo(HttpStatus.BAD_REQUEST))
	}
	@Test
	fun `家事実績が取得できる`() {
		val request = RegisterRequest("John", "hoge@hoge.com", "pass", "null")
		val responseRegister = restTemplate.postForEntity(
			"http://localhost:$port/api/auth/register", request,
			RegisterResponse::class.java
		)
		val token = responseRegister.body?.token
		val userId = responseRegister.body?.id
		val rawSql = javaClass.getResource("/test-seed.sql")!!.readText()
		val sql = rawSql.replace("{USER_ID}",userId.toString())
		jdbcTemplate.execute(sql)
		val headers = HttpHeaders().apply {
			set("Authorization", "Bearer $token")
		}
		val entity = HttpEntity<String>(headers)
		// List<TaskLogResponse>をResponseに指定するとKotlinの仕様でデシアライズ時に型消去される
		// ↓を使うとListの中身の型を保持した状態で渡せる
		val responseType = object : ParameterizedTypeReference<List<TaskLogResponse>>() {}
		val response = restTemplate.exchange(
			"http://localhost:$port/api/tasks/log?from=2025-06-02&to=2025-06-08",
			HttpMethod.GET,
			entity,
			responseType
		)
		assertThat(response.statusCode, equalTo(HttpStatus.OK))
	}
	@Test
	fun `存在しない場合はNO_CONTENT`() {
		val request = RegisterRequest("John", "hoge@hoge.com", "pass", "null")
		val responseRegister = restTemplate.postForEntity(
			"http://localhost:$port/api/auth/register", request,
			RegisterResponse::class.java
		)
		val token = responseRegister.body?.token
		val userId = responseRegister.body?.id
		val rawSql = javaClass.getResource("/test-seed.sql")!!.readText()
		val sql = rawSql.replace("{USER_ID}",userId.toString())
		jdbcTemplate.execute(sql)
		val headers = HttpHeaders().apply {
			set("Authorization", "Bearer $token")
		}
		val entity = HttpEntity<String>(headers)
		val responseType = object : ParameterizedTypeReference<List<TaskLogResponse>>() {}
		val response = restTemplate.exchange(
			"http://localhost:$port/api/tasks/log?from=2025-08-02&to=2025-09-08",
			HttpMethod.GET,
			entity,
			responseType
		)
		assertThat(response.statusCode, equalTo(HttpStatus.NO_CONTENT))
	}

	@Test
	fun `ポイント集計できる`(){
		val request = RegisterRequest("John", "hoge@hoge.com", "pass", "null")
		val responseRegister = restTemplate.postForEntity(
			"http://localhost:$port/api/auth/register", request,
			RegisterResponse::class.java
		)
		val token = responseRegister.body?.token
		val userId = responseRegister.body?.id
		val familyCode = responseRegister.body!!.familyCode
		val rawSql = javaClass.getResource("/test-seed.sql")!!.readText()
		val sql = rawSql.replace("{USER_ID}",userId.toString())
		jdbcTemplate.execute(sql)
		val headers = HttpHeaders().apply {
			set("Authorization", "Bearer $token")
		}
		val entity = HttpEntity<String>(headers)
		restTemplate.exchange("http://localhost:$port/api/auth/logout", HttpMethod.DELETE,entity, String::class.java)

		val requestSecond = RegisterRequest("Jiro", "fuga@fuga.com", "word", familyCode)
		val responseSecondRegister = restTemplate.postForEntity(
			"http://localhost:$port/api/auth/register", requestSecond,
			RegisterResponse::class.java
		)
		val tokenSecond = responseSecondRegister.body?.token
		val userIdSecond = responseSecondRegister.body?.id
		val familyId = responseRegister.body!!.familyID
		val sqlSecond = rawSql.replace("{USER_ID}",userIdSecond.toString())
		jdbcTemplate.execute(sqlSecond)
		val headersSecond = HttpHeaders().apply {
			set("Authorization", "Bearer $tokenSecond")
		}
		val entitySecond = HttpEntity<String>(headersSecond)

		val responseType = object : ParameterizedTypeReference<List<SummaryResponse>>() {}
		val response = restTemplate.exchange(
			"http://localhost:$port/api/summary?familyId=$familyId&from=2025-06-02&to=2025-06-08",
			HttpMethod.GET,
			entitySecond,
			responseType
		)

		assertThat(response.statusCode, equalTo(HttpStatus.OK))

	}

}
