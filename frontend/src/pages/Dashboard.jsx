import { useEffect, useState } from 'react';
import { Card, Row, Col, Statistic, Table, Tag, Typography, Spin } from 'antd';
import {
  ProjectOutlined,
  ThunderboltOutlined,
  DollarOutlined,
  TeamOutlined,
  TrophyOutlined,
  FlagOutlined,
} from '@ant-design/icons';
import { dashboardApi } from '../services/api';

const { Title } = Typography;

const statusColors = {
  '申请中': 'orange', '已立项': 'blue', '进行中': 'green',
  '已结题': 'default', '已终止': 'red',
};

export default function Dashboard() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    dashboardApi.getStats()
      .then(res => setStats(res.data))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <Spin size="large" style={{ display: 'block', margin: '100px auto' }} />;
  if (!stats) return null;

  const columns = [
    { title: '项目名称', dataIndex: 'title', key: 'title', ellipsis: true },
    { title: '负责人', dataIndex: 'principalInvestigator', key: 'pi', width: 100 },
    { title: '类别', dataIndex: 'category', key: 'category', width: 100,
      render: (t) => <Tag>{t}</Tag> },
    { title: '状态', dataIndex: 'status', key: 'status', width: 100,
      render: (s) => <Tag color={statusColors[s]}>{s}</Tag> },
    { title: '经费(万元)', dataIndex: 'funding', key: 'funding', width: 120,
      render: (v) => v?.toFixed(1) },
  ];

  return (
    <>
      <Title level={4} style={{ marginBottom: 24 }}>数据概览</Title>
      <Row gutter={[16, 16]}>
        <Col xs={24} sm={12} lg={4}>
          <Card hoverable size="small">
            <Statistic title="项目总数" value={stats.totalProjects}
              prefix={<ProjectOutlined />} valueStyle={{ color: '#1677ff' }} />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={4}>
          <Card hoverable size="small">
            <Statistic title="在研项目" value={stats.activeProjects}
              prefix={<ThunderboltOutlined />} valueStyle={{ color: '#52c41a' }} />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={4}>
          <Card hoverable size="small">
            <Statistic title="经费总额(万)" value={stats.totalFunding}
              prefix={<DollarOutlined />} precision={1} valueStyle={{ color: '#faad14' }} />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={4}>
          <Card hoverable size="small">
            <Statistic title="科研人员" value={stats.totalResearchers}
              prefix={<TeamOutlined />} valueStyle={{ color: '#722ed1' }} />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={4}>
          <Card hoverable size="small">
            <Statistic title="科研成果" value={stats.totalPublications}
              prefix={<TrophyOutlined />} valueStyle={{ color: '#13c2c2' }} />
          </Card>
        </Col>
        <Col xs={24} sm={12} lg={4}>
          <Card hoverable size="small">
            <Statistic title="已用经费(万)" value={stats.totalExpenditure}
              prefix={<DollarOutlined />} precision={1} valueStyle={{ color: '#cf1322' }} />
          </Card>
        </Col>
      </Row>

      <Row gutter={[16, 16]} style={{ marginTop: 24 }}>
        <Col xs={24} lg={8}>
          <Card title="项目状态分布" size="small">
            {Object.entries(stats.projectsByStatus).map(([k, v]) => (
              <div key={k} style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #f0f0f0' }}>
                <Tag color={statusColors[k]}>{k}</Tag>
                <span style={{ fontWeight: 600 }}>{v} 个</span>
              </div>
            ))}
          </Card>
        </Col>
        <Col xs={24} lg={8}>
          <Card title="项目类别分布" size="small">
            {Object.entries(stats.projectsByCategory).map(([k, v]) => (
              <div key={k} style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #f0f0f0' }}>
                <Tag color="processing">{k}</Tag>
                <span style={{ fontWeight: 600 }}>{v} 个</span>
              </div>
            ))}
          </Card>
        </Col>
        <Col xs={24} lg={8}>
          <Card title="成果类型分布" size="small">
            {stats.publicationsByType && Object.entries(stats.publicationsByType).map(([k, v]) => (
              <div key={k} style={{ display: 'flex', justifyContent: 'space-between', padding: '8px 0', borderBottom: '1px solid #f0f0f0' }}>
                <Tag color="cyan">{k}</Tag>
                <span style={{ fontWeight: 600 }}>{v} 项</span>
              </div>
            ))}
          </Card>
        </Col>
      </Row>

      <Card title="最近项目" size="small" style={{ marginTop: 24 }}>
        <Table dataSource={stats.recentProjects} columns={columns}
          rowKey="id" pagination={false} size="small" />
      </Card>
    </>
  );
}
