import { useEffect, useState } from 'react';
import {
  Table, Button, Modal, Form, Input, Select, InputNumber, Tag,
  Space, Typography, Popconfirm, message, Card, Row, Col,
} from 'antd';
import { PlusOutlined, SearchOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { projectApi } from '../services/api';

const { Title } = Typography;
const { TextArea } = Input;

const statusOptions = ['申请中', '已立项', '进行中', '已结题', '已终止'];
const categoryOptions = ['国家级', '省部级', '市厅级', '校级', '横向课题'];
const statusColors = {
  '申请中': 'orange', '已立项': 'blue', '进行中': 'green',
  '已结题': 'default', '已终止': 'red',
};

export default function Projects() {
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [filterStatus, setFilterStatus] = useState(null);
  const [form] = Form.useForm();

  const fetchProjects = async () => {
    setLoading(true);
    try {
      const params = {};
      if (searchKeyword) params.keyword = searchKeyword;
      if (filterStatus) params.status = filterStatus;
      const res = await projectApi.list(params);
      setProjects(res.data);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchProjects(); }, [searchKeyword, filterStatus]);

  const handleSave = async () => {
    const values = await form.validateFields();
    if (editing) {
      await projectApi.update(editing.id, values);
      message.success('更新成功');
    } else {
      await projectApi.create(values);
      message.success('创建成功');
    }
    setModalOpen(false);
    form.resetFields();
    setEditing(null);
    fetchProjects();
  };

  const handleEdit = (record) => {
    setEditing(record);
    form.setFieldsValue(record);
    setModalOpen(true);
  };

  const handleDelete = async (id) => {
    await projectApi.delete(id);
    message.success('删除成功');
    fetchProjects();
  };

  const columns = [
    { title: '项目编号', dataIndex: 'projectCode', key: 'code', width: 130 },
    { title: '项目名称', dataIndex: 'title', key: 'title', ellipsis: true },
    { title: '类别', dataIndex: 'category', key: 'category', width: 90,
      render: (t) => <Tag color="processing">{t}</Tag> },
    { title: '状态', dataIndex: 'status', key: 'status', width: 90,
      render: (s) => <Tag color={statusColors[s]}>{s}</Tag> },
    { title: '负责人', dataIndex: 'principalInvestigator', key: 'pi', width: 90 },
    { title: '部门', dataIndex: 'department', key: 'dept', width: 130, ellipsis: true },
    { title: '经费(万元)', dataIndex: 'funding', key: 'funding', width: 110,
      render: (v) => v?.toFixed(1) },
    { title: '操作', key: 'actions', width: 120, render: (_, r) => (
      <Space>
        <Button type="link" size="small" icon={<EditOutlined />} onClick={() => handleEdit(r)} />
        <Popconfirm title="确认删除?" onConfirm={() => handleDelete(r.id)}>
          <Button type="link" size="small" danger icon={<DeleteOutlined />} />
        </Popconfirm>
      </Space>
    )},
  ];

  return (
    <>
      <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: 16 }}>
        <Title level={4} style={{ margin: 0 }}>项目管理</Title>
        <Button type="primary" icon={<PlusOutlined />}
          onClick={() => { setEditing(null); form.resetFields(); setModalOpen(true); }}>
          新建项目
        </Button>
      </div>

      <Card size="small" style={{ marginBottom: 16 }}>
        <Row gutter={16}>
          <Col span={8}>
            <Input placeholder="搜索项目名称" prefix={<SearchOutlined />} allowClear
              onChange={(e) => setSearchKeyword(e.target.value)} />
          </Col>
          <Col span={6}>
            <Select placeholder="按状态筛选" allowClear style={{ width: '100%' }}
              onChange={(v) => setFilterStatus(v)}
              options={statusOptions.map(s => ({ label: s, value: s }))} />
          </Col>
        </Row>
      </Card>

      <Table dataSource={projects} columns={columns} rowKey="id" loading={loading}
        pagination={{ pageSize: 10, showTotal: (t) => `共 ${t} 个项目` }}
        scroll={{ x: 900 }} />

      <Modal title={editing ? '编辑项目' : '新建项目'} open={modalOpen} width={720}
        onOk={handleSave} onCancel={() => { setModalOpen(false); setEditing(null); }}
        okText="保存" cancelText="取消">
        <Form form={form} layout="vertical" style={{ marginTop: 16 }}>
          <Row gutter={16}>
            <Col span={16}>
              <Form.Item name="title" label="项目名称" rules={[{ required: true, message: '请输入项目名称' }]}>
                <Input />
              </Form.Item>
            </Col>
            <Col span={8}>
              <Form.Item name="projectCode" label="项目编号">
                <Input />
              </Form.Item>
            </Col>
          </Row>
          <Row gutter={16}>
            <Col span={8}>
              <Form.Item name="category" label="项目类别" rules={[{ required: true, message: '请选择类别' }]}>
                <Select options={categoryOptions.map(c => ({ label: c, value: c }))} />
              </Form.Item>
            </Col>
            <Col span={8}>
              <Form.Item name="status" label="项目状态" initialValue="申请中">
                <Select options={statusOptions.map(s => ({ label: s, value: s }))} />
              </Form.Item>
            </Col>
            <Col span={8}>
              <Form.Item name="principalInvestigator" label="项目负责人" rules={[{ required: true, message: '请输入负责人' }]}>
                <Input />
              </Form.Item>
            </Col>
          </Row>
          <Row gutter={16}>
            <Col span={8}>
              <Form.Item name="department" label="所属部门">
                <Input />
              </Form.Item>
            </Col>
            <Col span={8}>
              <Form.Item name="funding" label="经费(万元)">
                <InputNumber style={{ width: '100%' }} min={0} precision={1} />
              </Form.Item>
            </Col>
            <Col span={8}>
              <Form.Item name="fundingSource" label="经费来源">
                <Input />
              </Form.Item>
            </Col>
          </Row>
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item name="startDate" label="开始日期">
                <Input placeholder="YYYY-MM-DD" />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item name="endDate" label="结束日期">
                <Input placeholder="YYYY-MM-DD" />
              </Form.Item>
            </Col>
          </Row>
          <Form.Item name="members" label="项目成员">
            <Input placeholder="多人以逗号分隔" />
          </Form.Item>
          <Form.Item name="description" label="项目描述">
            <TextArea rows={3} />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
}
