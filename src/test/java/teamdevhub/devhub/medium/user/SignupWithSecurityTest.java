package teamdevhub.devhub.medium.user;
//
//@WebMvcTest(UserController.class)
//@Import({
//        UserQueryService.class,
//        SecurityConfig.class
//})
//public class SignupWithSecurityTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserRepositoryPort userRepositoryPort;
//
//    @Test
//    @WithMockUser(username = "user1", roles = "USER")
//    void 인증된_사용자는_본인정보를_조회할_수_있다() throws Exception {
//        // given
//        given(userRepositoryPort.findById(any()))
//                .willReturn(Optional.of(
//                        User.create("test@test.com", "pw", "tester")
//                ));
//
//        // when & then
//        mockMvc.perform(get("/users/me"))
//                .andExpect(status().isOk());
//    }
//}