import { useEffect, useState } from 'react';
import {
  Table, Button, Modal, Form, Input, Select, Tag, Space, Typography,
  Popconfirm, message, Card, Row, Col,
} from 'antd';
import { PlusOutlined, SearchOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { publicationApi } from '../services/api';

const { Title } = Typography;
const { TextArea } = Input;

const typeOptions = ['论文', '专利', '获奖', '著作', '报告'];
const typeColors = { '论文': 'blue', '专利': 'green', '获奖': 'gold', '著作': 'purple', '报告': 'cyan' };

export default function Publications() {
  const [list, setList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [filterType, setFilterType] = useState(null);
  const [form] = Form.useForm();

  const fetchData = async () => {
    setLoading(true);
    try {
      const params = {};
      if (searchKeyword) params.keyword = searchKeyword;
      if (filterType) params.type = filterType;
      const res = await publicationApi.list(params);
      setList(res.data);
    } finally { setLoading(false); }
  };

  useEffect(() => { fetchData(); }, [searchKeyword, filterType]);

  const handleSave = async () => {
    const values = await form.validateFields();
    if (editing) {
      await publicationApi.update(editing.id, values);
      message.success('更新成功');
    } else {
      await publicationApi.create(values);
      message.success('创建成功');
    }
    setModalOpen(false); form.resetFields(); setEditing(null);
    fetchData();
  };

  const handleEdit = (r) => { setEditing(r); form.setFieldsValue(r); setModalOpen(true); };
  const handleDelete = async (id) => { await publicationApi.delete(id); message.success('删除成功'); fetchData(); };

  const columns = [
    { title: '成果名称', dataIndex: 'title', key: 'title', ellipsis: true },
    { title: '类型', dataIndex: 'type', key: 'type', width: 80,
      render: (t) => <Tag color={typeColors[t]}>{t}</Tag> },
    { title: '作者', dataIndex: 'authors', key: 'authors', width: 120, ellipsis: true },
    { title: '发表刊物', dataIndex: 'journal', key: 'journal', width: 160, ellipsis: true,
      render: (v) => v || '-' },
    { title: '影响因子', dataIndex: 'impactFactor', key: 'if', width: 90,
      render: (v) => v || '-' },
    { title: '关联项目', dataIndex: 'projectTitle', key: 'proj', width: 160, ellipsis: true,
      render: (v) => v || '-' },
    { title: '发表日期', dataIndex: 'publishDate', key: 'date', width: 110 },
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
        <Title level={4} style={{ margin: 0 }}>成果管理</Title>
        <Button type="primary" icon={<PlusOutlined />}
          onClick={() => { setEditing(null); form.resetFields(); setModalOpen(true); }}>
          添加成果
        </Button>
      </div>
      <Card size="small" style={{ marginBottom: 16 }}>
        <Row gutter={16}>
          <Col span={8}>
            <Input placeholder="搜索成果名称" prefix={<SearchOutlined />} allowClear
              onChange={(e) => setSearchKeyword(e.target.value)} />
          </Col>
          <Col span={6}>
            <Select placeholder="按类型筛选" allowClear style={{ width: '100%' }}
              onChange={(v) => setFilterType(v)}
              options={typeOptions.map(s => ({ label: s, value: s }))} />
          </Col>
        </Row>
      </Card>
      <Table dataSource={list} columns={columns} rowKey="id" loading={loading}
        pagination={{ pageSize: 10, showTotal: (t) => `共 ${t} 项成果` }}
        scroll={{ x: 900 }} />
      <Modal title={editing ? '编辑成果' : '添加成果'} open={modalOpen} width={680}
        onOk={handleSave} onCancel={() => { setModalOpen(false); setEditing(null); }}
        okText="保存" cancelText="取消">
        <Form form={form} layout="vertical" style={{ marginTop: 16 }}>
          <Form.Item name="title" label="成果名称" rules={[{ required: true, message: '请输入成果名称' }]}>
            <Input />
          </Form.Item>
          <Row gutter={16}>
            <Col span={8}>
              <Form.Item name="type" label="成果类型" rules={[{ required: true, message: '请选择类型' }]}>
                <Select options={typeOptions.map(t => ({ label: t, value: t }))} />
              </Form.Item>
            </Col>
            <Col span={8}>
              <Form.Item name="authors" label="作者">
                <Input />
              </Form.Item>
            </Col>
            <Col span={8}>
              <Form.Item name="publishDate" label="发表日期">
                <Input placeholder="YYYY-MM-DD" />
              </Form.Item>
            </Col>
          </Row>
          <Row gutter={16}>
            <Col span={12}>
              <Form.Item name="journal" label="发表刊物/授权机构">
                <Input />
              </Form.Item>
            </Col>
            <Col span={6}>
              <Form.Item name="impactFactor" label="影响因子">
                <Input />
              </Form.Item>
            </Col>
            <Col span={6}>
              <Form.Item name="doi" label="DOI">
                <Input />
              </Form.Item>
            </Col>
          </Row>
          <Row gutter={16}>
            <Col span={8}>
              <Form.Item name="projectId" label="关联项目ID">
                <Input type="number" />
              </Form.Item>
            </Col>
            <Col span={16}>
              <Form.Item name="projectTitle" label="关联项目名称">
                <Input />
              </Form.Item>
            </Col>
          </Row>
          <Form.Item name="abstractText" label="摘要">
            <TextArea rows={3} />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
}
