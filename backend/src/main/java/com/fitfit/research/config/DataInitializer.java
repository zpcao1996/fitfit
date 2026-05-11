package com.fitfit.research.config;

import com.fitfit.research.entity.*;
import com.fitfit.research.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ResearcherRepository researcherRepository;
    private final PublicationRepository publicationRepository;
    private final MilestoneRepository milestoneRepository;
    private final AnnouncementRepository announcementRepository;
    private final ExpenditureRepository expenditureRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, ProjectRepository projectRepository,
                           ResearcherRepository researcherRepository, PublicationRepository publicationRepository,
                           MilestoneRepository milestoneRepository, AnnouncementRepository announcementRepository,
                           ExpenditureRepository expenditureRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.researcherRepository = researcherRepository;
        this.publicationRepository = publicationRepository;
        this.milestoneRepository = milestoneRepository;
        this.announcementRepository = announcementRepository;
        this.expenditureRepository = expenditureRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedResearchers();
        seedProjects();
        seedPublications();
        seedMilestones();
        seedAnnouncements();
        seedExpenditures();
    }

    private void seedUsers() {
        if (userRepository.count() > 0) return;
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFullName("系统管理员");
        admin.setEmail("admin@fitfit.com");
        admin.setAdmin(true);
        userRepository.save(admin);
    }

    private void seedResearchers() {
        if (researcherRepository.count() > 0) return;
        String[][] data = {
            {"张三", "R001", "教授", "计算机科学学院", "zhangsan@university.edu", "人工智能"},
            {"李四", "R002", "副教授", "材料科学学院", "lisi@university.edu", "纳米材料"},
            {"王五", "R003", "讲师", "生物医学学院", "wangwu@university.edu", "基因工程"},
            {"赵六", "R004", "教授", "物理学院", "zhaoliu@university.edu", "量子计算"},
            {"陈七", "R005", "副教授", "化学学院", "chenqi@university.edu", "有机化学"},
        };
        for (String[] r : data) {
            Researcher researcher = new Researcher();
            researcher.setName(r[0]); researcher.setEmployeeId(r[1]);
            researcher.setTitle(r[2]); researcher.setDepartment(r[3]);
            researcher.setEmail(r[4]); researcher.setResearchField(r[5]);
            researcherRepository.save(researcher);
        }
    }

    private void seedProjects() {
        if (projectRepository.count() > 0) return;
        Object[][] data = {
            {"基于深度学习的医学影像智能诊断系统", "NF2024-001", "国家级", "进行中", "张三", "计算机科学学院", 150.0, "国家自然科学基金", "2024-01-01", "2027-12-31"},
            {"新型碳纳米管复合材料的制备与应用", "NF2024-002", "国家级", "进行中", "李四", "材料科学学院", 80.0, "国家自然科学基金", "2024-03-01", "2027-02-28"},
            {"CRISPR基因编辑技术在农业中的应用", "PF2024-001", "省部级", "已立项", "王五", "生物医学学院", 45.0, "省科技厅", "2024-06-01", "2026-05-31"},
            {"量子纠错码的理论与实验研究", "NF2023-005", "国家级", "进行中", "赵六", "物理学院", 200.0, "国家重点研发计划", "2023-01-01", "2026-12-31"},
            {"绿色有机催化剂的设计与合成", "PF2024-003", "省部级", "申请中", "陈七", "化学学院", 30.0, "省自然科学基金", "2024-09-01", "2026-08-31"},
            {"智慧城市大数据分析平台建设", "HZ2024-001", "横向课题", "进行中", "张三", "计算机科学学院", 120.0, "某科技公司", "2024-02-01", "2025-12-31"},
            {"高性能锂硫电池关键材料研究", "XJ2024-001", "校级", "已结题", "李四", "材料科学学院", 10.0, "校级科研基金", "2023-06-01", "2024-05-31"},
        };
        for (Object[] p : data) {
            Project project = new Project();
            project.setTitle((String) p[0]); project.setProjectCode((String) p[1]);
            project.setCategory((String) p[2]); project.setStatus((String) p[3]);
            project.setPrincipalInvestigator((String) p[4]); project.setDepartment((String) p[5]);
            project.setFunding((Double) p[6]); project.setFundingSource((String) p[7]);
            project.setStartDate((String) p[8]); project.setEndDate((String) p[9]);
            projectRepository.save(project);
        }
    }

    private void seedPublications() {
        if (publicationRepository.count() > 0) return;
        Object[][] data = {
            {"Deep Learning Approach for Medical Image Diagnosis", "论文", "张三, 李四", "Nature Machine Intelligence", "2024-06-15", "10.1038/s42256-024-001", 1L, "基于深度学习的医学影像智能诊断系统", "25.3"},
            {"Carbon Nanotube Composite: A Novel Synthesis Method", "论文", "李四, 王五", "Advanced Materials", "2024-08-20", "10.1002/adma.2024001", 2L, "新型碳纳米管复合材料的制备与应用", "29.4"},
            {"Quantum Error Correction with Topological Codes", "论文", "赵六", "Physical Review Letters", "2024-03-10", "10.1103/PhysRevLett.001", 4L, "量子纠错码的理论与实验研究", "8.6"},
            {"基于CRISPR的作物抗病性改良方法", "专利", "王五, 陈七", null, "2024-09-01", null, 3L, "CRISPR基因编辑技术在农业中的应用", null},
            {"新型碳纳米管制备工艺", "专利", "李四", null, "2024-07-15", null, 2L, "新型碳纳米管复合材料的制备与应用", null},
            {"Smart City Data Analytics Platform Design", "论文", "张三", "IEEE Transactions on Big Data", "2024-11-01", "10.1109/TBDATA.2024.001", 6L, "智慧城市大数据分析平台建设", "7.2"},
            {"量子纠错码研究获省科技进步二等奖", "获奖", "赵六", null, "2024-12-01", null, 4L, "量子纠错码的理论与实验研究", null},
        };
        for (Object[] d : data) {
            Publication pub = new Publication();
            pub.setTitle((String) d[0]); pub.setType((String) d[1]);
            pub.setAuthors((String) d[2]); pub.setJournal((String) d[3]);
            pub.setPublishDate((String) d[4]); pub.setDoi((String) d[5]);
            pub.setProjectId((Long) d[6]); pub.setProjectTitle((String) d[7]);
            pub.setImpactFactor((String) d[8]);
            publicationRepository.save(pub);
        }
    }

    private void seedMilestones() {
        if (milestoneRepository.count() > 0) return;
        Object[][] data = {
            {1L, "基于深度学习的医学影像智能诊断系统", "数据集采集与标注", "完成10万张医学影像的采集与标注", "2024-06-30", "2024-06-25", "已完成", 100},
            {1L, "基于深度学习的医学影像智能诊断系统", "模型训练与优化", "完成深度学习模型的训练，准确率达到95%以上", "2025-03-31", null, "进行中", 65},
            {1L, "基于深度学习的医学影像智能诊断系统", "临床验证与部署", "在3家医院完成临床验证", "2026-12-31", null, "未开始", 0},
            {2L, "新型碳纳米管复合材料的制备与应用", "材料合成方法研究", "确定最优合成路线", "2024-09-30", "2024-09-15", "已完成", 100},
            {2L, "新型碳纳米管复合材料的制备与应用", "性能表征与测试", "完成材料力学、导电性能测试", "2025-06-30", null, "进行中", 40},
            {4L, "量子纠错码的理论与实验研究", "理论框架建立", "完成拓扑量子纠错码理论分析", "2024-06-30", "2024-05-20", "已完成", 100},
            {4L, "量子纠错码的理论与实验研究", "实验验证", "在量子处理器上验证纠错方案", "2025-12-31", null, "进行中", 30},
            {6L, "智慧城市大数据分析平台建设", "需求分析与系统设计", "完成系统架构设计文档", "2024-04-30", "2024-04-28", "已完成", 100},
            {6L, "智慧城市大数据分析平台建设", "核心模块开发", "完成数据采集、清洗、分析模块", "2025-06-30", null, "进行中", 55},
        };
        for (Object[] d : data) {
            Milestone m = new Milestone();
            m.setProjectId((Long) d[0]); m.setProjectTitle((String) d[1]);
            m.setTitle((String) d[2]); m.setDescription((String) d[3]);
            m.setDueDate((String) d[4]); m.setCompletedDate((String) d[5]);
            m.setStatus((String) d[6]); m.setProgress((Integer) d[7]);
            milestoneRepository.save(m);
        }
    }

    private void seedAnnouncements() {
        if (announcementRepository.count() > 0) return;
        Object[][] data = {
            {"2025年度国家自然科学基金申报通知", "各位老师：2025年度国家自然科学基金项目申报工作已经开始，请于2025年3月15日前完成网上填报。具体申报指南请参见基金委网站。联系人：科研处张老师，电话：12345678。", "通知", "科研处", true, "2025-01-10"},
            {"关于开展2024年度科研项目中期检查的通知", "根据学校科研管理规定，将对2024年在研项目进行中期检查。请各项目负责人于2025年4月30日前提交中期检查报告。", "通知", "科研处", true, "2025-02-15"},
            {"我校张三教授团队在Nature子刊发表重要成果", "近日，计算机科学学院张三教授团队在Nature Machine Intelligence上发表了题为\"Deep Learning Approach for Medical Image Diagnosis\"的研究论文，该成果在医学影像智能诊断领域取得重要突破。", "新闻", "宣传部", false, "2024-07-01"},
            {"省科技进步奖申报通知", "2025年度省科技进步奖申报工作即将启动，请有意申报的老师提前准备材料。详情请咨询科研处。", "通知", "科研处", false, "2025-03-01"},
            {"学术讲座：量子计算的前沿与挑战", "主讲人：赵六教授。时间：2025年4月20日 14:00-16:00。地点：理学楼报告厅。欢迎全校师生参加。", "活动", "物理学院", false, "2025-04-10"},
        };
        for (Object[] d : data) {
            Announcement a = new Announcement();
            a.setTitle((String) d[0]); a.setContent((String) d[1]);
            a.setType((String) d[2]); a.setAuthor((String) d[3]);
            a.setPinned((Boolean) d[4]); a.setPublishDate((String) d[5]);
            announcementRepository.save(a);
        }
    }

    private void seedExpenditures() {
        if (expenditureRepository.count() > 0) return;
        Object[][] data = {
            {1L, "基于深度学习的医学影像智能诊断系统", 15.0, "设备费", "GPU服务器采购（A100×4）", "张三", "2024-03-15", "已通过"},
            {1L, "基于深度学习的医学影像智能诊断系统", 3.5, "材料费", "医学影像数据采集存储硬盘", "张三", "2024-05-20", "已通过"},
            {1L, "基于深度学习的医学影像智能诊断系统", 2.0, "差旅费", "参加MICCAI国际会议", "张三", "2024-10-01", "已通过"},
            {2L, "新型碳纳米管复合材料的制备与应用", 8.0, "材料费", "实验试剂及耗材采购", "李四", "2024-04-10", "已通过"},
            {2L, "新型碳纳米管复合材料的制备与应用", 5.0, "测试化验费", "TEM/SEM表征测试", "李四", "2024-08-15", "已通过"},
            {4L, "量子纠错码的理论与实验研究", 25.0, "设备费", "低温量子测量设备升级", "赵六", "2024-02-01", "已通过"},
            {4L, "量子纠错码的理论与实验研究", 5.0, "差旅费", "参加APS March Meeting", "赵六", "2024-03-10", "已通过"},
            {6L, "智慧城市大数据分析平台建设", 12.0, "设备费", "云服务器集群租赁（一年）", "张三", "2024-03-01", "已通过"},
            {6L, "智慧城市大数据分析平台建设", 8.0, "劳务费", "研究生助研津贴", "张三", "2024-06-01", "已通过"},
            {4L, "量子纠错码的理论与实验研究", 3.0, "出版费", "PRL论文版面费及开放获取费", "赵六", "2024-04-15", "待审批"},
            {3L, "CRISPR基因编辑技术在农业中的应用", 6.5, "材料费", "基因编辑试剂盒及培养基", "王五", "2025-01-10", "待审批"},
        };
        for (Object[] d : data) {
            Expenditure e = new Expenditure();
            e.setProjectId((Long) d[0]); e.setProjectTitle((String) d[1]);
            e.setAmount((Double) d[2]); e.setCategory((String) d[3]);
            e.setDescription((String) d[4]); e.setApplicant((String) d[5]);
            e.setExpenseDate((String) d[6]); e.setStatus((String) d[7]);
            expenditureRepository.save(e);
        }
    }
}
