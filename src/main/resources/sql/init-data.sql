INSERT INTO common_code (CODE_ID, NAME, SUPERIOR_CODE_ID, SORT_ORDER, USE_YN, REGISTERED_DATE, MODIFIED_DATE, REGISTRANT_GUID, MODIFIER_GUID, REMARKS) VALUES
                                                                                                                                                           ('SKILL_CODE', '기술 스택', '0000', 1, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 'Root'),
                                                                                                                                                           ('POSITION_CODE', '포지션', '0000', 2, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 'Root'),
                                                                                                                                                           ('POSITION_LEVEL_CODE', '숙련도', '0000', 3, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 'Root'),
                                                                                                                                                           ('PROJECT_RECRUIT_TYPE', '모집 유형', '0000', 4, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 'Root'),
                                                                                                                                                           ('PROJECT_PROGRESS_TYPE', '진행 방식', '0000', 5, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 'Root'),
                                                                                                                                                           ('PROJECT_RECRUIT_STATUS', '모집 상태', '0000', 6, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 'Root'),
                                                                                                                                                           ('PROJECT_APPROVAL_STATUS', '승인 상태', '0000', 7, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 'Root'),
                                                                                                                                                           ('BOARD_CATEGORY', '게시글 카테고리', '0000', 8, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 'Root'),
                                                                                                                                                           ('REPORT_TYPE', '신고 유형', '0000', 9, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 'Root'),
                                                                                                                                                           ('NOTIFICATION_TYPE', '알림 유형', '0000', 10, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 'Root'),
                                                                                                                                                           ('REGION_CODE', '지역 코드', '0000', 11, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', 'Root');

INSERT INTO common_code (CODE_ID, NAME, SUPERIOR_CODE_ID, SORT_ORDER, USE_YN, REGISTERED_DATE, MODIFIED_DATE, REGISTRANT_GUID, MODIFIER_GUID, REMARKS) VALUES
('1001', 'Java', 'SKILL_CODE', 1, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('1002', 'Spring', 'SKILL_CODE', 2, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('1003', 'React', 'SKILL_CODE', 3, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('1004', 'Node.js', 'SKILL_CODE', 4, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('2001', 'Backend', 'POSITION_CODE', 1, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('2002', 'Frontend', 'POSITION_CODE', 2, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('2003', 'Fullstack', 'POSITION_CODE', 3, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('2004', 'Mobile', 'POSITION_CODE', 4, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('2101', '초급', 'POSITION_LEVEL_CODE', 1, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('2102', '중급', 'POSITION_LEVEL_CODE', 2, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('2103', '고급', 'POSITION_LEVEL_CODE', 3, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('3001', '일반모집', 'PROJECT_RECRUIT_TYPE', 1, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('3002', '추가모집', 'PROJECT_RECRUIT_TYPE', 2, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('3101', '온라인', 'PROJECT_PROGRESS_TYPE', 1, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('3102', '오프라인', 'PROJECT_PROGRESS_TYPE', 2, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('3103', '온/오프라인 병행', 'PROJECT_PROGRESS_TYPE', 3, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('3201', '모집중', 'PROJECT_RECRUIT_STATUS', 1, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('3202', '모집완료', 'PROJECT_RECRUIT_STATUS', 2, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('3203', '모집대기', 'PROJECT_RECRUIT_STATUS', 3, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('3301', '승인 대기', 'PROJECT_APPROVAL_STATUS', 1, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('3302', '승인 완료', 'PROJECT_APPROVAL_STATUS', 2, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('3303', '승인 거절', 'PROJECT_APPROVAL_STATUS', 3, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('4001', '자유게시판', 'BOARD_CATEGORY', 1, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('4002', '질문', 'BOARD_CATEGORY', 2, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('4003', '공지사항', 'BOARD_CATEGORY', 3, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('5001', '스팸/광고', 'REPORT_TYPE', 1, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('5002', '욕설/비방', 'REPORT_TYPE', 2, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('5003', '불법 콘텐츠', 'REPORT_TYPE', 3, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('6001', '프로젝트 지원 알림', 'NOTIFICATION_TYPE', 1, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('6002', '프로젝트 승인 알림', 'NOTIFICATION_TYPE', 2, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
('6003', '댓글 알림', 'NOTIFICATION_TYPE', 3, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', '');


INSERT INTO common_code (CODE_ID, NAME, SUPERIOR_CODE_ID, SORT_ORDER, USE_YN, REGISTERED_DATE, MODIFIED_DATE, REGISTRANT_GUID, MODIFIER_GUID, REMARKS) VALUES
                                                                                                                                                           ('8100', '서울', 'REGION_CODE', 1, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('8200', '경기', 'REGION_CODE', 2, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('8300', '인천', 'REGION_CODE', 3, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('8400', '부산', 'REGION_CODE', 4, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', '');

INSERT INTO common_code (CODE_ID, NAME, SUPERIOR_CODE_ID, SORT_ORDER, USE_YN, REGISTERED_DATE, MODIFIED_DATE, REGISTRANT_GUID, MODIFIER_GUID, REMARKS) VALUES
                                                                                                                                                           ('810001', '강남구', '8100', 1, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810002', '강동구', '8100', 2, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810003', '강북구', '8100', 3, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810004', '강서구', '8100', 4, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810005', '관악구', '8100', 5, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810006', '광진구', '8100', 6, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810007', '구로구', '8100', 7, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810008', '금천구', '8100', 8, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810009', '노원구', '8100', 9, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810010', '도봉구', '8100', 10, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810011', '동대문구', '8100', 11, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810012', '동작구', '8100', 12, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810013', '마포구', '8100', 13, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810014', '서대문구', '8100', 14, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810015', '서초구', '8100', 15, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810016', '성동구', '8100', 16, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810017', '성북구', '8100', 17, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810018', '송파구', '8100', 18, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810019', '양천구', '8100', 19, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810020', '영등포구', '8100', 20, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810021', '용산구', '8100', 21, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810022', '은평구', '8100', 22, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810023', '종로구', '8100', 23, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810024', '중구', '8100', 24, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('810025', '중랑구', '8100', 25, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', '');

INSERT INTO common_code (CODE_ID, NAME, SUPERIOR_CODE_ID, SORT_ORDER, USE_YN, REGISTERED_DATE, MODIFIED_DATE, REGISTRANT_GUID, MODIFIER_GUID, REMARKS) VALUES
                                                                                                                                                           ('820001', '수원시', '8200', 1, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('820002', '성남시', '8200', 2, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('820003', '고양시', '8200', 3, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('820004', '용인시', '8200', 4, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('820005', '부천시', '8200', 5, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('820006', '안산시', '8200', 6, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('820007', '안양시', '8200', 7, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('820008', '남양주시', '8200', 8, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('820009', '화성시', '8200', 9, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('820010', '평택시', '8200', 10, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('820011', '의정부시', '8200', 11, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('820012', '시흥시', '8200', 12, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('820013', '파주시', '8200', 13, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('820014', '김포시', '8200', 14, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('820015', '광명시', '8200', 15, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('820016', '군포시', '8200', 16, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('820017', '이천시', '8200', 17, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('820018', '양주시', '8200', 18, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('820019', '오산시', '8200', 19, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('820020', '구리시', '8200', 20, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', '');

INSERT INTO common_code (CODE_ID, NAME, SUPERIOR_CODE_ID, SORT_ORDER, USE_YN, REGISTERED_DATE, MODIFIED_DATE, REGISTRANT_GUID, MODIFIER_GUID, REMARKS) VALUES
                                                                                                                                                           ('830001', '중구', '8300', 1, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('830002', '동구', '8300', 2, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('830003', '미추홀구', '8300', 3, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('830004', '연수구', '8300', 4, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('830005', '남동구', '8300', 5, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('830006', '부평구', '8300', 6, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('830007', '계양구', '8300', 7, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('830008', '서구', '8300', 8, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', '');

INSERT INTO common_code (CODE_ID, NAME, SUPERIOR_CODE_ID, SORT_ORDER, USE_YN, REGISTERED_DATE, MODIFIED_DATE, REGISTRANT_GUID, MODIFIER_GUID, REMARKS) VALUES
                                                                                                                                                           ('840001', '해운대구', '8400', 1, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('840002', '수영구', '8400', 2, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('840003', '남구', '8400', 3, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('840004', '동래구', '8400', 4, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('840005', '부산진구', '8400', 5, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('840006', '사하구', '8400', 6, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('840007', '북구', '8400', 7, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('840008', '사상구', '8400', 8, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', ''),
                                                                                                                                                           ('840009', '강서구', '8400', 9, 'Y', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'SYSTEM', 'SYSTEM', '');



INSERT INTO terms (
    terms_guid,
    name,
    content,
    required_yn,
    use_yn,
    delete_yn
) VALUES (
             '8f3a2b1c4d5e6f708192a3b4c5d6e7f8',
             '서비스 이용약관',
             '<h1>서비스 이용약관</h1>

             <h2>제1조 (목적)</h2>
             <p>본 약관은 DevHub(이하 "회사")가 제공하는 사이드 프로젝트 구인 플랫폼 서비스(이하 "서비스")의 이용과 관련하여 회사와 회원 간의 권리, 의무 및 책임사항을 규정함을 목적으로 합니다.</p>

             <h2>제2조 (정의)</h2>
             <ul>
                 <li>"회원"이란 본 약관에 동의하고 서비스에 가입하여 이용하는 자를 말합니다.</li>
                 <li>"프로젝트"란 회원이 서비스 내에서 등록하는 사이드 프로젝트 모집 정보를 말합니다.</li>
                 <li>"지원"이란 회원이 프로젝트에 참여 의사를 표시하는 행위를 말합니다.</li>
             </ul>

             <h2>제3조 (약관의 효력 및 변경)</h2>
             <ol>
                 <li>본 약관은 회원이 회원가입 시 동의함으로써 효력이 발생합니다.</li>
                 <li>회사는 관련 법령을 위반하지 않는 범위에서 약관을 변경할 수 있으며, 변경 시 서비스 내 공지합니다.</li>
             </ol>

             <h2>제4조 (회원가입 및 계정관리)</h2>
             <ol>
                 <li>회원가입은 OAuth 로그인 또는 이메일 인증 방식을 통해 이루어집니다.</li>
                 <li>회원은 가입 시 제공한 정보가 정확하고 최신의 정보임을 보장해야 합니다.</li>
                 <li>회원은 본인의 계정을 제3자에게 양도하거나 공유할 수 없습니다.</li>
             </ol>

             <h2>제5조 (서비스 제공)</h2>
             <ol>
                 <li>회사는 프로젝트 모집, 지원, 관리 기능을 제공합니다.</li>
                 <li>회사는 서비스 개선 또는 운영상 필요에 따라 서비스의 일부를 변경하거나 중단할 수 있습니다.</li>
             </ol>

             <h2>제6조 (회원의 의무)</h2>
             <p>회원은 다음 행위를 하여서는 안 됩니다.</p>
             <ol>
                 <li>허위 또는 타인의 정보를 이용한 가입</li>
                 <li>서비스의 정상적인 운영을 방해하는 행위</li>
                 <li>타 회원에게 불쾌감이나 피해를 주는 행위</li>
                 <li>관련 법령을 위반하는 행위</li>
             </ol>

             <h2>제7조 (이용 제한 및 탈퇴)</h2>
             <ol>
                 <li>회사는 회원이 본 약관을 위반한 경우 서비스 이용을 제한할 수 있습니다.</li>
                 <li>회원은 언제든지 서비스 내 기능을 통해 탈퇴할 수 있습니다.</li>
             </ol>

             <h2>제8조 (책임의 제한)</h2>
             <ol>
                 <li>회사는 회원 간에 발생한 분쟁에 대하여 개입하지 않으며, 이에 대한 책임을 지지 않습니다.</li>
                 <li>회사는 천재지변, 시스템 장애 등 불가항력적 사유로 인한 서비스 중단에 책임을 지지 않습니다.</li>
             </ol>

             <h2>제9조 (준거법 및 관할)</h2>
             <p>본 약관은 대한민국 법률을 따르며, 서비스 이용과 관련한 분쟁은 관할 법원에 따릅니다.</p>',
             'Y',
             'Y',
             'N'
         );


INSERT INTO terms (
    terms_guid,
    name,
    content,
    required_yn,
    use_yn,
    delete_yn
) VALUES (
             '1a2b3c4d5e6f70819283746556473829',
             '개인정보처리방침',
             '<h1>개인정보처리방침</h1>

             <p>DevHub(이하 "회사")은 개인정보보호법 등 관련 법령을 준수하며, 회원의 개인정보를 보호하기 위해 다음과 같은 개인정보처리방침을 수립·공개합니다.</p>

             <h2>1. 수집하는 개인정보 항목</h2>
             <ul>
                 <li>필수항목: 이메일 주소</li>
                 <li>OAuth 로그인 시 제공되는 이메일 정보</li>
             </ul>

             <h2>2. 개인정보 수집 목적</h2>
             <ul>
                 <li>회원 식별 및 로그인 처리</li>
                 <li>서비스 제공 및 운영</li>
                 <li>서비스 관련 공지사항 전달</li>
             </ul>

             <h2>3. 개인정보 보관 및 이용 기간</h2>
             <ul>
                 <li>회원 탈퇴 시 즉시 삭제</li>
                 <li>단, 관련 법령에 따라 보관이 필요한 경우 해당 기간 동안 보관</li>
             </ul>

             <h2>4. 개인정보의 제3자 제공</h2>
             <p>회사는 원칙적으로 회원의 개인정보를 외부에 제공하지 않습니다.</p>

             <h2>5. 개인정보 처리 위탁</h2>
             <ul>
                 <li>OAuth 인증 제공자 (Google, GitHub 등)</li>
                 <li>이메일 발송 서비스 (사용하는 경우)</li>
             </ul>

             <h2>6. 회원의 권리</h2>
             <ul>
                 <li>회원은 언제든지 개인정보 열람, 수정, 삭제를 요청할 수 있습니다.</li>
                 <li>회원은 서비스 내 탈퇴 기능을 통해 개인정보 삭제를 요청할 수 있습니다.</li>
             </ul>

             <h2>7. 개인정보 보호를 위한 조치</h2>
             <ul>
                 <li>접근 권한 최소화</li>
                 <li>개인정보 저장 시 암호화 또는 이에 준하는 보호 조치</li>
             </ul>

             <h2>8. 개인정보 보호책임자</h2>
             <ul>
                 <li>담당자: DevHub</li>
                 <li>문의: devhub@example.com</li>
             </ul>',
             'Y',
             'Y',
             'N'
         );

INSERT INTO application_form( MODIFIED_DATE, REGISTERED_DATE, APPLICATION_FORM_GUID, CUSTOM_YN, HELP_TEXT, MODIFIER_GUID, REGISTRANT_GUID, TITLE, TYPE_CD, USE_YN) VALUES
                                                                                                                                                                       (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1', 'N', '이름을 입력하세요', '1', '1', '이름', 'textfield_100', 'Y'),
                                                                                                                                                                       (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '2', 'N', '나이를 입력하세요', '1', '1', '나이', 'textfield_100', 'Y'),
                                                                                                                                                                       (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '3', 'N', '자기소개를 입력하세요', '1', '1', '자기소개', 'textarea', 'Y'),
                                                                                                                                                                       (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '4', 'N', '성별을 선택하세요', '1', '1', '성별', 'checkbox', 'Y');