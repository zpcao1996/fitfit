import { useEffect, useState } from 'react';
import {
  Table, Button, Modal, Form, Input, Tag, Space, Typography, Popconfirm, message, Card,
} from 'antd';
import { PlusOutlined, SearchOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons';
import { researcherApi } from '../services/api';

const { Title } = Typography;

export default function Researchers() {
  const [researchers, setResearchers] = useState([]);
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [searchKeyword, setSearchKeyword] = useState('');
  const [form] = Form.useForm();

  const fetchResearchers = async () => {
    setLoading(true);
    try {
      const params = {};
      if (searchKeyword) params.keyword = searchKeyword;
      const res = await researcherApi.list(params);
      setResearchers(res.data);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchResearchers(); }, [searchKeyword]);

  const handleSave = async () => {
    const values = await form.validateFields();
    if (editing) {
      await researcherApi.update(editing.id, values);
      message.success('更新成功');
    } else {
      await researcherApi.create(values);
      message.success('创建成功');
    }
    setModalOpen(false);
    form.resetFields();
    setEditing(null);
    fetchResearchers();
  };

  const handleEdit = (record) => {
    setEditing(record);
    form.setFieldsValue(record);
    setModalOpen(true);
  };

  const handleDelete = async (id) => {
    await researcherApi.delete(id);
    message.success('删除成功');
    fetchResearchers();
  };

  const columns = [
    { title: '工号', dataIndex: 'employeeId', key: 'eid', width: 100 },
    { title: '姓名', dataIndex: 'name', key: 'name', width: 100 },
    { title: '职称', dataIndex: 'title', key: 'title', width: 100,
      render: (t) => t ? <Tag color="blue">{t}</Tag> : '-' },
    { title: '部门', dataIndex: 'department', key: 'dept', width: 150 },
    { title: '邮箱', dataIndex: 'email', key: 'email', width: 200, ellipsis: true },
    { title: '研究方向', dataIndex: 'researchField', key: 'field', ellipsis: true },
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
        <Title level={4} style={{ margin: 0 }}>科研人员管理</Title>
        <Button type="primary" icon={<PlusOutlined />}
          onClick={() => { setEditing(null); form.resetFields(); setModalOpen(true); }}>
          添加人员
        </Button>
      </div>

      <Card size="small" style={{ marginBottom: 16 }}>
        <Input placeholder="搜索姓名" prefix={<SearchOutlined />} allowClear
          style={{ width: 300 }}
          onChange={(e) => setSearchKeyword(e.target.value)} />
      </Card>

      <Table dataSource={researchers} columns={columns} rowKey="id" loading={loading}
        pagination={{ pageSize: 10, showTotal: (t) => `共 ${t} 人` }}
        scroll={{ x: 800 }} />

      <Modal title={editing ? '编辑人员' : '添加人员'} open={modalOpen}
        onOk={handleSave} onCancel={() => { setModalOpen(false); setEditing(null); }}
        okText="保存" cancelText="取消">
        <Form form={form} layout="vertical" style={{ marginTop: 16 }}>
          <Form.Item name="name" label="姓名" rules={[{ required: true, message: '请输入姓名' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="employeeId" label="工号">
            <Input />
          </Form.Item>
          <Form.Item name="title" label="职称">
            <Input placeholder="如：教授、副教授、讲师" />
          </Form.Item>
          <Form.Item name="department" label="所属部门">
            <Input />
          </Form.Item>
          <Form.Item name="email" label="邮箱">
            <Input />
          </Form.Item>
          <Form.Item name="phone" label="电话">
            <Input />
          </Form.Item>
          <Form.Item name="researchField" label="研究方向">
            <Input />
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
}
