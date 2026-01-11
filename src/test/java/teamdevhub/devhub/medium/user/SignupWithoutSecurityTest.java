package teamdevhub.devhub.medium.user;

//@WebMvcTest(UserController.class)
//@Import(SignupUserService.class)
//public class SignupWithoutSecurityTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserRepositoryPort userRepositoryPort;
//
//    @MockBean
//    private PasswordEncoderPort passwordEncoderPort;
//
//    @Test
//    void 회원가입_요청이_들어오면_유스케이스를_거쳐_저장_요청이_전달된다() throws Exception {
//        // given
//        given(passwordEncoderPort.encode(any()))
//                .willReturn("encoded-password");
//
//        String request = """
//            {
//              "email": "test@test.com",
//              "password": "1234",
//              "name": "tester"
//            }
//            """;
//
//        // when
//        mockMvc.perform(post("/users/signup")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(request))
//                .andExpect(status().isCreated());
//
//        // then
//        verify(userRepositoryPort, times(1)).save(any(User.class));
//    }
//}
