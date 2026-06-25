from typing import Dict, List, Tuple
import networkx as nx


def load_gml(path: str) -> Tuple[nx.Graph, Dict]:
    try:
        G_raw = nx.read_gml(path)

        mapping_nodekey_to_index = {}
        original_id_map = {}
        for new_idx, (node_key, data) in enumerate(G_raw.nodes(data=True)):
            if isinstance(data, dict) and 'id' in data:
                original_id = data['id']
            elif isinstance(data, dict) and 'label' in data:
                original_id = data['label']
            else:
                original_id = node_key

            mapping_nodekey_to_index[node_key] = new_idx
            original_id_map[original_id] = new_idx

        G = nx.relabel_nodes(G_raw, mapping_nodekey_to_index, copy=True)

        if G.is_directed():
            G = G.to_undirected()

        return G, original_id_map
    except Exception:
        nodes = {}
        edges = []
        with open(path, 'r', encoding='utf-8') as fh:
            state = None
            cur = {}
            for raw in fh:
                line = raw.strip()
                if not line:
                    continue
                if line.startswith('node'):
                    state = 'node'
                    cur = {}
                    continue
                if line.startswith('edge'):
                    state = 'edge'
                    cur = {}
                    continue
                if line == ']':
                    if state == 'node' and 'id' in cur:
                        nodes[cur['id']] = cur.get('label')
                    if state == 'edge' and 'source' in cur and 'target' in cur:
                        s = cur['source']
                        t = cur['target']
                        edges.append((s, t))
                    state = None
                    cur = {}
                    continue
                if state in ('node', 'edge'):
                    parts = line.split(None, 1)
                    if len(parts) == 2:
                        key, val = parts
                        try:
                            if val.startswith('"') and val.endswith('"'):
                                valp = val.strip('"')
                            else:
                                valp = int(val)
                        except Exception:
                            valp = val.strip('"')
                        cur[key] = valp

        original_ids = list(nodes.keys())
        id_to_idx = {orig: i for i, orig in enumerate(sorted(original_ids))}
        G = nx.Graph()
        for orig, lbl in nodes.items():
            G.add_node(id_to_idx[orig])
        for s, t in edges:
            if s in id_to_idx and t in id_to_idx and s != t:
                G.add_edge(id_to_idx[s], id_to_idx[t])

        original_id_map = {orig: idx for orig, idx in id_to_idx.items()}
        return G, original_id_map


def load_real_dat(path: str) -> List[List[int]]:
    communities: List[List[int]] = []
    with open(path, 'r', encoding='utf-8') as fh:
        for line in fh:
            line = line.strip()
            if not line:
                continue
            parts = line.split()
            community = [int(tok) - 1 for tok in parts]
            communities.append(community)
    return communities


def load_class_labels(path: str, n_nodes: int) -> List[int]:
    labels = [-1] * n_nodes
    with open(path, 'r', encoding='utf-8') as fh:
        for line in fh:
            line = line.strip()
            if not line:
                continue
            parts = line.split()
            if len(parts) < 2:
                continue
            node_id = int(parts[0]) - 1
            comm = int(parts[1])
            if 0 <= node_id < n_nodes:
                labels[node_id] = comm
    return labels


def validate_consistency(G: nx.Graph, communities: List[List[int]] = None, labels: List[int] = None) -> Dict:
    result = {}
    nodes_in_G = set(G.nodes())
    result['n_nodes'] = len(nodes_in_G)
    if communities is not None:
        nodes_in_real = set()
        for c in communities:
            nodes_in_real.update(c)
        result['nodes_in_real_count'] = len(nodes_in_real)
        result['missing_in_graph_from_real'] = sorted(list(nodes_in_real - nodes_in_G))
        result['missing_in_real_from_graph'] = sorted(list(nodes_in_G - nodes_in_real))
    if labels is not None:
        nodes_in_labels = set(i for i, v in enumerate(labels) if v != -1)
        result['nodes_in_labels_count'] = len(nodes_in_labels)
        result['missing_in_graph_from_labels'] = sorted(list(nodes_in_labels - nodes_in_G))
        result['missing_in_labels_from_graph'] = sorted(list(nodes_in_G - nodes_in_labels))
    return result
