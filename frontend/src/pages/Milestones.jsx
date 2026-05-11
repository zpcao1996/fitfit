import { useEffect, useState } from 'react';
import {
  Table, Button, Modal, Form, Input, Select, InputNumber, Tag, Space,
  Typography, Popconfirm, message, Card, Row, Col, Progress,
} from 'antd';
import { PlusOutlined, SearchOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { milestoneApi } from '../services/api';

const { Title } = Typography;
const { TextArea } = Input;

const statusOptions = ['未开始', '进行中', '已完成', '已延期'];
const statusColors = { '未开始': 'default', '进行中': 'processing', '已完成': 'success', '已延期': 'error' };

export default function Milestones() {
  const [list, setList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [filterStatus, setFilterStatus] = useState(null);
  const [filterProjectId, setFilterProjectId] = useState(null);
  const [form] = Form.useForm();

  const fetchData = async () => {
    setLoading(true);
    try {
      const params = {};
      if (filterStatus) params.status = filterStatus;
      if (filterProjectId) params.projectId = filterProjectId;
      const res = await milestoneApi.list(params);
      setList(res.data);
    } finally { setLoading(false); }
  };

  useEffect(() => { fetchData(); }, [filterStatus, filterProjectId]);

  const handleSave = async () => {
    const values = await form.validateFields();
    if (editing) {
      await milestoneApi.update(editing.id, values);
      message.success('更新成功');
    } else {
      await milestoneApi.create(values);
      message.success('创建成功');
    }
    setModalOpen(false); form.resetFields(); setEditing(null);
    fetchData();
  };

  const handleEdit = (r) => { setEditing(r); form.setFieldsValue(r); setModalOpen(true); };
  const handleDelete = async (id) => { await milestoneApi.delete(id); message.success('删除成功'); fetchData(); };

  const columns = [
    { title: '里程碑', dataIndex: 'title', key: 'title', ellipsis: true },
    { title: '所属项目', dataIndex: 'projectTitle', key: 'proj', width: 180, ellipsis: true },
    { title: '状态', dataIndex: 'status', key: 'status', width: 90,
      render: (s) => <Tag color={statusColors[s]}>{s}</Tag> },
    { title: '进度', dataIndex: 'progress', key: 'progress', width: 140,
      render: (v) => <Progress percent={v} size="small" status={v === 100 ? 'success' : 'active'} /> },
    { title: '截止日期', dataIndex: 'dueDate', key: 'due', width: 110 },
    { title: '完成日期', dataIndex: 'completedDate', key: 'done', width: 110,
      render: (v) => v || '-' },
    { title: '操作', key: 'actions', width: 100, render: (_, r) => (
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
        <Title level={4} style={{ margin: 0 }}>项目进度管理</Title>
        <Button type="primary" icon={<PlusOutlined />}
          onClick={() => { setEditing(null); form.resetFields(); setModalOpen(true); }}>
          添加里程碑
        </Button>
      </div>
      <Card size="small" style={{ marginBottom: 16 }}>
        <Row gutter={16}>
          <Col span={6}>
            <Select placeholder="按状态筛选" allowClear style={{ width: '100%' }}
              onChange={(v) => setFilterStatus(v)}
              options={statusOptions.map(s => ({ label: s, value: s }))} />
          </Col>
          <Col span={6}>
            <Input placeholder="按项目ID筛选" allowClear type="number"
              onChange={(e) => setFilterProjectId(e.target.value || null)} />
          </Col>
        </Row>
      </Card>
      <Table dataSource={list} columns={columns} rowKey="id" loading={loading}
        pagination={{ pageSize: 10, showTotal: (t) => `共 ${t} 个里程碑` }}
        scroll={{ x: 850 }} />
      <Modal title={editing ? '编辑里程碑' : '添加里程碑'} open={modalOpen} width={640}
        onOk={handleSave} onCancel={() => { setModalOpen(false); setEditing(null); }}
        okText="保存" cancelText="取消">
        <Form form={form} layout="vertical" style={{ marginTop: 16 }}>
          <Form.Item name="title" label="里程碑名称" rules={[{ required: true, message: '请输入名称' }]}>
            <Input />
          </Form.Item>
          <Row gutter={16}>
            <Col span={8}>
              <Form.Item name="projectId" label="项目ID" rules={[{ required: true, message: '请输入项目ID' }]}>
                <InputNumber style={{ width: '100%' }} min={1} />
              </Form.Item>
            </Col>
            <Col span={16}>
              <Form.Item name="projectTitle" label="项目名称">
                <Input />
              </Form.Item>
            </Col>
          </Row>
          <Row gutter={16}>
            <Col span={8}>
              <Form.Item name="status" label="状态" initialValue="未开始">
                <Select options={statusOptions.map(s => ({ label: s, value: s }))} />
              </Form.Item>
            </Col>
            <Col span={8}>
              <Form.Item name="progress" label="进度(%)" initialValue={0}>
                <InputNumber style={{ width: '100%' }} min={0} max={100} />
              </Form.Item>
            </Col>
            <Col span={8}>
              <Form.Item name="dueDate" label="截止日期">
                <Input placeholder="YYYY-MM-DD" />
              </Form.Item>
            </Col>
          </Row>
          <Form.Item name="completedDate" label="完成日期">
            <Input placeholder="YYYY-MM-DD" />
          </Form.Item>
          <Form.Item name="description" label="描述">
            <TextArea rows={3} />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
}
