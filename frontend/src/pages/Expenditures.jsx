import { useEffect, useState } from 'react';
import {
  Table, Button, Modal, Form, Input, Select, InputNumber, Tag, Space,
  Typography, Popconfirm, message, Card, Row, Col,
} from 'antd';
import { PlusOutlined, EditOutlined, DeleteOutlined, CheckOutlined, CloseOutlined } from '@ant-design/icons';
import { expenditureApi } from '../services/api';

const { Title } = Typography;

const categoryOptions = ['设备费', '材料费', '测试化验费', '差旅费', '会议费', '劳务费', '出版费', '其他'];
const statusOptions = ['待审批', '已通过', '已驳回'];
const statusColors = { '待审批': 'orange', '已通过': 'green', '已驳回': 'red' };

export default function Expenditures() {
  const [list, setList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [filterStatus, setFilterStatus] = useState(null);
  const [filterCategory, setFilterCategory] = useState(null);
  const [form] = Form.useForm();

  const fetchData = async () => {
    setLoading(true);
    try {
      const params = {};
      if (filterStatus) params.status = filterStatus;
      if (filterCategory) params.category = filterCategory;
      const res = await expenditureApi.list(params);
      setList(res.data);
    } finally { setLoading(false); }
  };

  useEffect(() => { fetchData(); }, [filterStatus, filterCategory]);

  const handleSave = async () => {
    const values = await form.validateFields();
    if (editing) {
      await expenditureApi.update(editing.id, values);
      message.success('更新成功');
    } else {
      await expenditureApi.create(values);
      message.success('创建成功');
    }
    setModalOpen(false); form.resetFields(); setEditing(null);
    fetchData();
  };

  const handleEdit = (r) => { setEditing(r); form.setFieldsValue(r); setModalOpen(true); };
  const handleDelete = async (id) => { await expenditureApi.delete(id); message.success('删除成功'); fetchData(); };

  const handleApprove = async (id, status) => {
    await expenditureApi.update(id, { status });
    message.success(status === '已通过' ? '审批通过' : '已驳回');
    fetchData();
  };

  const columns = [
    { title: '关联项目', dataIndex: 'projectTitle', key: 'proj', width: 180, ellipsis: true },
    { title: '费用类别', dataIndex: 'category', key: 'cat', width: 100,
      render: (t) => <Tag>{t}</Tag> },
    { title: '金额(万元)', dataIndex: 'amount', key: 'amount', width: 110,
      render: (v) => <span style={{ fontWeight: 600, color: '#cf1322' }}>{v?.toFixed(1)}</span> },
    { title: '用途说明', dataIndex: 'description', key: 'desc', ellipsis: true },
    { title: '申请人', dataIndex: 'applicant', key: 'applicant', width: 80 },
    { title: '日期', dataIndex: 'expenseDate', key: 'date', width: 110 },
    { title: '状态', dataIndex: 'status', key: 'status', width: 90,
      render: (s) => <Tag color={statusColors[s]}>{s}</Tag> },
    { title: '操作', key: 'actions', width: 150, render: (_, r) => (
      <Space>
        {r.status === '待审批' && (
          <>
            <Button type="link" size="small" icon={<CheckOutlined />}
              style={{ color: '#52c41a' }}
              onClick={() => handleApprove(r.id, '已通过')} />
            <Button type="link" size="small" icon={<CloseOutlined />}
              danger onClick={() => handleApprove(r.id, '已驳回')} />
          </>
        )}
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
        <Title level={4} style={{ margin: 0 }}>经费使用管理</Title>
        <Button type="primary" icon={<PlusOutlined />}
          onClick={() => { setEditing(null); form.resetFields(); setModalOpen(true); }}>
          新增支出
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
            <Select placeholder="按费用类别筛选" allowClear style={{ width: '100%' }}
              onChange={(v) => setFilterCategory(v)}
              options={categoryOptions.map(c => ({ label: c, value: c }))} />
          </Col>
        </Row>
      </Card>
      <Table dataSource={list} columns={columns} rowKey="id" loading={loading}
        pagination={{ pageSize: 10, showTotal: (t) => `共 ${t} 条记录` }}
        scroll={{ x: 950 }} />
      <Modal title={editing ? '编辑支出' : '新增支出'} open={modalOpen} width={640}
        onOk={handleSave} onCancel={() => { setModalOpen(false); setEditing(null); }}
        okText="保存" cancelText="取消">
        <Form form={form} layout="vertical" style={{ marginTop: 16 }}>
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
              <Form.Item name="amount" label="金额(万元)" rules={[{ required: true, message: '请输入金额' }]}>
                <InputNumber style={{ width: '100%' }} min={0} precision={1} />
              </Form.Item>
            </Col>
            <Col span={8}>
              <Form.Item name="category" label="费用类别" rules={[{ required: true, message: '请选择类别' }]}>
                <Select options={categoryOptions.map(c => ({ label: c, value: c }))} />
              </Form.Item>
            </Col>
            <Col span={8}>
              <Form.Item name="status" label="状态" initialValue="待审批">
                <Select options={statusOptions.map(s => ({ label: s, value: s }))} />
              </Form.Item>
            </Col>
          </Row>
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item name="applicant" label="申请人">
                <Input />
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item name="expenseDate" label="日期">
                <Input placeholder="YYYY-MM-DD" />
              </Form.Item>
            </Col>
          </Row>
          <Form.Item name="description" label="用途说明">
            <Input />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
}
