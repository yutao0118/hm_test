package cn.itcast.hmall.service.impl;

import cn.itcast.hmall.mapper.ItemMapper;
import cn.itcast.hmall.pojo.RequestParams;
import cn.itcast.hmall.pojo.hmallDoc;
import cn.itcast.hmall.service.IItemService;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.common.dto.Item;
import com.hmall.common.dto.PageDTO;
import lombok.SneakyThrows;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemService extends ServiceImpl<ItemMapper, Item> implements IItemService {

    @Autowired
    private RestHighLevelClient client;

    /**
     * 分页查询
     * @param params
     * @return
     */
    @SneakyThrows
    @Override
    public PageDTO<hmallDoc> search(RequestParams params) {
        Integer page = params.getPage();
        Integer size = params.getSize();

        SearchRequest request = new SearchRequest("hmall");
        buildBasicQuery(params, request);
        Integer index = (page - 1) * size;
        request.source().from(index).size(size);
        //2.发送请求给ES
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        //3.处理返回结果
        PageDTO<hmallDoc> dto = handleResponse(response);
        return dto;
    }

    /**
     * 自动补全
     * @param key
     * @return
     */
    @SneakyThrows
    @Override
    public List<String> getkey(String key) {
        SearchRequest request = new SearchRequest("hmall");
        request.source().suggest(new SuggestBuilder().addSuggestion("suggestions", SuggestBuilders
                .completionSuggestion("suggestion")
                .prefix(key)
                .size(10)
                .skipDuplicates(true)));

        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        Suggest suggest = response.getSuggest();
        CompletionSuggestion suggestions = suggest.getSuggestion("suggestions");
        List<CompletionSuggestion.Entry.Option> options = suggestions.getOptions();
        List<String> list = new ArrayList<>(options.size());
        for (CompletionSuggestion.Entry.Option option : options) {
            String text = option.getText().toString();
            list.add(text);
        }
        return list;
    }

    @SneakyThrows
    @Override
    public Map<String, List<String>> fifters(RequestParams param) {
        SearchRequest request = new SearchRequest("hmall");
        buildBasicQuery(param, request);
        request.source().size(0);
        buildAggregation(request);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        HashMap<String, List<String>> map = new HashMap<>();
        Aggregations aggregations = response.getAggregations();
        List<String> brandAgg = getAggByName(aggregations, "brandAgg");
        map.put("brand", brandAgg);
        List<String> categoryAgg = getAggByName(aggregations, "categoryAgg");
        map.put("category", categoryAgg);
        return map;
    }

    /**
     * 处理聚合结果
     *
     * @param aggregations
     * @param name
     */
    private List<String> getAggByName(Aggregations aggregations, String name) {
        Terms aggregation = aggregations.get(name);
        List<? extends Terms.Bucket> list = aggregation.getBuckets();
        ArrayList<String> list1 = new ArrayList<>();
        for (Terms.Bucket bucket : list) {
            String keyAsString = bucket.getKeyAsString();
            list1.add(keyAsString);
        }
        return list1;
    }

    /**
     * 构建聚合函数
     * @param request
     */
    private void buildAggregation(SearchRequest request) {
        request.source().aggregation(AggregationBuilders
                .terms("brandAgg")
                .size(30)
                .field("brand"));
        request.source().aggregation(AggregationBuilders
                .terms("categoryAgg")
                .size(30)
                .field("category"));
    }

    /**
     * 处理返回数据
     *
     * @param response
     * @return
     */
    private PageDTO<hmallDoc> handleResponse(SearchResponse response) {
        // 获取命中的所有内容
        SearchHits searchHits = response.getHits();
        // 获取命中的总条数
        long total = searchHits.getTotalHits().value;
        System.out.println("命中的条数为: " + total);
        List<hmallDoc> list = new ArrayList<>();
        // 获取命中的文档对象数组
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            // 解析每一个hit对象得到对应的文档数据
            String json = hit.getSourceAsString();
            hmallDoc hmallDoc = JSON.parseObject(json, hmallDoc.class);
            // 解析酒店距我的距离
            Object[] sortValues = hit.getSortValues();
            if (sortValues != null && sortValues.length > 0) {
                hmallDoc.setDistance(sortValues[0]);
            }
            list.add(hmallDoc);
            System.out.println(hmallDoc);
        }
        return new PageDTO<hmallDoc>(total, list);
    }

    /**
     * 封装请求的DSL语句
     *
     * @param params
     * @param request
     */
    private void buildBasicQuery(RequestParams params, SearchRequest request) {
        // 1.构建BooleanQuery
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        // 2.关键字搜索
        String key = params.getKey();
        if (key == null || "".equals(key)) {
            boolQuery.must(QueryBuilders.matchAllQuery());
        } else {
            boolQuery.must(QueryBuilders.matchQuery("all", key));
        }
        // 3.分类
        if (params.getCategory() != null && !params.getCategory().equals("")) {
            boolQuery.filter(QueryBuilders.termQuery("category", params.getCategory()));
        }
        // 4.品牌条件
        if (params.getBrand() != null && !params.getBrand().equals("")) {
            boolQuery.filter(QueryBuilders.termQuery("brand", params.getBrand()));
        }
        // 6.价格
        if (params.getMinPrice() != null && params.getMaxPrice() != null) {
            boolQuery.filter(QueryBuilders
                    .rangeQuery("price")
                    .gte(params.getMinPrice()*100)
                    .lte(params.getMaxPrice()*100)
            );
        }
        // 算分函数查询
        FunctionScoreQueryBuilder functionScoreQueryBuilder =
                QueryBuilders.functionScoreQuery(
                        boolQuery, // 基础查询(bool查询)
                        new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{ // 构建算分函数
                                new FunctionScoreQueryBuilder.FilterFunctionBuilder(
                                        QueryBuilders.termQuery("isAD", true), // 过滤条件,满足此条件则进行重写算分
                                        ScoreFunctionBuilders.weightFactorFunction(10000) // 权重
                                )
                        }
                );
        // 7.放入source
        request.source().query(functionScoreQueryBuilder);
    }

    /**
     * 同步添加或修改
     * @param id
     */
    @SneakyThrows
    public void insetInes(Long id) {
        UpdateRequest request = new UpdateRequest("hmall",id.toString());
        Item item = this.getById(id);
        hmallDoc hmallDoc = new hmallDoc(item);
        String jsonString = JSON.toJSONString(hmallDoc);
        request.doc(jsonString, XContentType.JSON);
        client.update(request, RequestOptions.DEFAULT);
    }

    /**
     * 同步删除
     * @param id
     */
    @SneakyThrows
    public void delInes(Long id) {
        DeleteRequest request = new DeleteRequest("hmall", id.toString());
        client.delete(request, RequestOptions.DEFAULT);
    }
}
