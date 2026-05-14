import { useEffect, useState } from 'react';
import {
  Table, Button, Modal, Form, Input, Select, Tag, Switch, Space,
  Typography, Popconfirm, message, Card, Row, Col,
} from 'antd';
import { PlusOutlined, SearchOutlined, EditOutlined, DeleteOutlined, PushpinOutlined } from '@ant-design/icons';
import { announcementApi } from '../services/api';

const { Title } = Typography;
const { TextArea } = Input;

const typeOptions = ['通知', '新闻', '活动', '公示'];
const typeColors = { '通知': 'red', '新闻': 'blue', '活动': 'green', '公示': 'orange' };

export default function Announcements() {
  const [list, setList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [detailOpen, setDetailOpen] = useState(false);
  const [detail, setDetail] = useState(null);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [filterType, setFilterType] = useState(null);
  const [form] = Form.useForm();

  const fetchData = async () => {
    setLoading(true);
    try {
      const params = {};
      if (searchKeyword) params.keyword = searchKeyword;
      if (filterType) params.type = filterType;
      const res = await announcementApi.list(params);
      setList(res.data);
    } finally { setLoading(false); }
  };

  useEffect(() => { fetchData(); }, [searchKeyword, filterType]);

  const handleSave = async () => {
    const values = await form.validateFields();
    if (editing) {
      await announcementApi.update(editing.id, values);
      message.success('更新成功');
    } else {
      await announcementApi.create(values);
      message.success('创建成功');
    }
    setModalOpen(false); form.resetFields(); setEditing(null);
    fetchData();
  };

  const handleEdit = (r) => { setEditing(r); form.setFieldsValue(r); setModalOpen(true); };
  const handleDelete = async (id) => { await announcementApi.delete(id); message.success('删除成功'); fetchData(); };

  const columns = [
    { title: '', dataIndex: 'pinned', key: 'pin', width: 40,
      render: (v) => v ? <PushpinOutlined style={{ color: '#f5222d' }} /> : null },
    { title: '标题', dataIndex: 'title', key: 'title', ellipsis: true,
      render: (text, r) => (
        <a onClick={() => { setDetail(r); setDetailOpen(true); }}>{text}</a>
      )},
    { title: '类型', dataIndex: 'type', key: 'type', width: 80,
      render: (t) => <Tag color={typeColors[t]}>{t}</Tag> },
    { title: '发布人', dataIndex: 'author', key: 'author', width: 100 },
    { title: '发布日期', dataIndex: 'publishDate', key: 'date', width: 110 },
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
        <Title level={4} style={{ margin: 0 }}>通知公告</Title>
        <Button type="primary" icon={<PlusOutlined />}
          onClick={() => { setEditing(null); form.resetFields(); setModalOpen(true); }}>
          发布公告
        </Button>
      </div>
      <Card size="small" style={{ marginBottom: 16 }}>
        <Row gutter={16}>
          <Col span={8}>
            <Input placeholder="搜索标题" prefix={<SearchOutlined />} allowClear
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
        pagination={{ pageSize: 10, showTotal: (t) => `共 ${t} 条公告` }}
        scroll={{ x: 700 }} />

      <Modal title="公告详情" open={detailOpen} footer={null}
        onCancel={() => setDetailOpen(false)} width={640}>
        {detail && (
          <div style={{ padding: '8px 0' }}>
            <Title level={4}>{detail.title}</Title>
            <div style={{ marginBottom: 16, color: '#999' }}>
              <Tag color={typeColors[detail.type]}>{detail.type}</Tag>
              {detail.author} · {detail.publishDate}
              {detail.pinned && <Tag color="red" style={{ marginLeft: 8 }}>置顶</Tag>}
            </div>
            <div style={{ lineHeight: 1.8, whiteSpace: 'pre-wrap' }}>{detail.content}</div>
          </div>
        )}
      </Modal>

      <Modal title={editing ? '编辑公告' : '发布公告'} open={modalOpen} width={640}
        onOk={handleSave} onCancel={() => { setModalOpen(false); setEditing(null); }}
        okText="保存" cancelText="取消">
        <Form form={form} layout="vertical" style={{ marginTop: 16 }}>
          <Form.Item name="title" label="标题" rules={[{ required: true, message: '请输入标题' }]}>
            <Input />
          </Form.Item>
          <Row gutter={16}>
            <Col span={8}>
              <Form.Item name="type" label="类型" rules={[{ required: true, message: '请选择类型' }]}>
                <Select options={typeOptions.map(t => ({ label: t, value: t }))} />
              </Form.Item>
            </Col>
            <Col span={8}>
              <Form.Item name="author" label="发布人">
                <Input />
              </Form.Item>
            </Col>
            <Col span={4}>
              <Form.Item name="pinned" label="置顶" valuePropName="checked">
                <Switch />
              </Form.Item>
            </Col>
            <Col span={4}>
              <Form.Item name="publishDate" label="日期">
                <Input placeholder="YYYY-MM-DD" />
              </Form.Item>
            </Col>
          </Row>
          <Form.Item name="content" label="内容" rules={[{ required: true, message: '请输入内容' }]}>
            <TextArea rows={6} />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
}
