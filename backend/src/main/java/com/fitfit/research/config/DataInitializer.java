package com.fitfit.research.config;

import com.fitfit.research.entity.Project;
import com.fitfit.research.entity.Researcher;
import com.fitfit.research.entity.User;
import com.fitfit.research.repository.ProjectRepository;
import com.fitfit.research.repository.ResearcherRepository;
import com.fitfit.research.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ResearcherRepository researcherRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, ProjectRepository projectRepository,
                           ResearcherRepository researcherRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.researcherRepository = researcherRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("系统管理员");
            admin.setEmail("admin@fitfit.com");
            admin.setAdmin(true);
            userRepository.save(admin);
        }

        if (researcherRepository.count() == 0) {
            String[][] researchers = {
                {"张三", "R001", "教授", "计算机科学学院", "zhangsan@university.edu", "人工智能"},
                {"李四", "R002", "副教授", "材料科学学院", "lisi@university.edu", "纳米材料"},
                {"王五", "R003", "讲师", "生物医学学院", "wangwu@university.edu", "基因工程"},
                {"赵六", "R004", "教授", "物理学院", "zhaoliu@university.edu", "量子计算"},
                {"陈七", "R005", "副教授", "化学学院", "chenqi@university.edu", "有机化学"},
            };
            for (String[] r : researchers) {
                Researcher researcher = new Researcher();
                researcher.setName(r[0]);
                researcher.setEmployeeId(r[1]);
                researcher.setTitle(r[2]);
                researcher.setDepartment(r[3]);
                researcher.setEmail(r[4]);
                researcher.setResearchField(r[5]);
                researcherRepository.save(researcher);
            }
        }

        if (projectRepository.count() == 0) {
            Object[][] projects = {
                {"基于深度学习的医学影像智能诊断系统", "NF2024-001", "国家级", "进行中", "张三", "计算机科学学院", 150.0, "国家自然科学基金", "2024-01-01", "2027-12-31"},
                {"新型碳纳米管复合材料的制备与应用", "NF2024-002", "国家级", "进行中", "李四", "材料科学学院", 80.0, "国家自然科学基金", "2024-03-01", "2027-02-28"},
                {"CRISPR基因编辑技术在农业中的应用", "PF2024-001", "省部级", "已立项", "王五", "生物医学学院", 45.0, "省科技厅", "2024-06-01", "2026-05-31"},
                {"量子纠错码的理论与实验研究", "NF2023-005", "国家级", "进行中", "赵六", "物理学院", 200.0, "国家重点研发计划", "2023-01-01", "2026-12-31"},
                {"绿色有机催化剂的设计与合成", "PF2024-003", "省部级", "申请中", "陈七", "化学学院", 30.0, "省自然科学基金", "2024-09-01", "2026-08-31"},
                {"智慧城市大数据分析平台建设", "HZ2024-001", "横向课题", "进行中", "张三", "计算机科学学院", 120.0, "某科技公司", "2024-02-01", "2025-12-31"},
                {"高性能锂硫电池关键材料研究", "XJ2024-001", "校级", "已结题", "李四", "材料科学学院", 10.0, "校级科研基金", "2023-06-01", "2024-05-31"},
            };
            for (Object[] p : projects) {
                Project project = new Project();
                project.setTitle((String) p[0]);
                project.setProjectCode((String) p[1]);
                project.setCategory((String) p[2]);
                project.setStatus((String) p[3]);
                project.setPrincipalInvestigator((String) p[4]);
                project.setDepartment((String) p[5]);
                project.setFunding((Double) p[6]);
                project.setFundingSource((String) p[7]);
                project.setStartDate((String) p[8]);
                project.setEndDate((String) p[9]);
                projectRepository.save(project);
            }
        }
    }
}
