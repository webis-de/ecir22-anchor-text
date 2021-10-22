package de.webis.trec_dl_21_ltr.lightgbm;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.SneakyThrows;

@Data
public class LightGBMConfiguration {

  private final String trainFile = "docv2_train", testFile = "2021_trec_dl_test";
  
  private final List<String> validationFiles = Arrays.asList("docv2_dev1", "docv2_dev2");
  
  private final double featureFraction = 1.0;

  @SneakyThrows
  public void writeTrainConfigFile(Path dir) {
    Files.write(dir.resolve("train.conf"), trainConfiguration().getBytes());
  }
  
  private String trainConfiguration() {
    return "# task type, support train and predict\n"
    		+ "task = train\n"
    		+ "\n"
    		+ "# boosting type, support gbdt for now, alias: boosting, boost\n"
    		+ "boosting_type = gbdt\n"
    		+ "\n"
    		+ "# application type, support following application\n"
    		+ "# regression , regression task\n"
    		+ "# binary , binary classification task\n"
    		+ "# lambdarank , LambdaRank task\n"
    		+ "# alias: application, app\n"
    		+ "objective = lambdarank\n"
    		+ "\n"
    		+ "# eval metrics, support multi metric, delimited by ',' , support following metrics\n"
    		+ "# l1 \n"
    		+ "# l2 , default metric for regression\n"
    		+ "# ndcg , default metric for lambdarank\n"
    		+ "# auc \n"
    		+ "# binary_logloss , default metric for binary\n"
    		+ "# binary_error\n"
    		+ "metric = ndcg\n"
    		+ "\n"
    		+ "# evaluation position for ndcg metric, alias : ndcg_at\n"
    		+ "ndcg_eval_at = 1,3,5\n"
    		+ "\n"
    		+ "# frequency for metric output\n"
    		+ "metric_freq = 1\n"
    		+ "\n"
    		+ "# true if need output metric for training data, alias: tranining_metric, train_metric\n"
    		+ "is_training_metric = true\n"
    		+ "\n"
    		+ "# column in data to use as label\n"
    		+ "label_column = 0\n"
    		+ "\n"
    		+ "# query information for training data\n"
    		+ "query_id = 1\n"
    		+ "\n"
    		+ "# number of bins for feature bucket, 255 is a recommend setting, it can save memories, and also has good accuracy. \n"
    		+ "max_bin = 255\n"
    		+ "\n"
    		+ "# training data\n"
    		+ "# if existing weight file, should name to \"rank.train.weight\"\n"
    		+ "# if existing query file, should name to \"rank.train.query\"\n"
    		+ "# alias: train_data, train\n"
    		+ "data = " + trainFile + "\n"
    		+ "\n"
    		+ "# validation data, support multi validation data, separated by ','\n"
    		+ "# if existing weight file, should name to \"rank.test.weight\"\n"
    		+ "# if existing query file, should name to \"rank.test.query\"\n"
    		+ "# alias: valid, test, test_data, \n"
    		+ "valid_data = " + validationFiles.stream().collect(Collectors.joining(",")) + "\n"
    		+ "\n"
    		+ "# number of trees(iterations), alias: num_tree, num_iteration, num_iterations, num_round, num_rounds\n"
    		+ "num_trees = 100\n"
    		+ "\n"
    		+ "# shrinkage rate , alias: shrinkage_rate\n"
    		+ "learning_rate = 0.1\n"
    		+ "\n"
    		+ "# number of leaves for one tree, alias: num_leaf\n"
    		+ "num_leaves = 31\n"
    		+ "\n"
    		+ "# type of tree learner, support following types:\n"
    		+ "# serial , single machine version\n"
    		+ "# feature , use feature parallel to train\n"
    		+ "# data , use data parallel to train\n"
    		+ "# voting , use voting based parallel to train\n"
    		+ "# alias: tree\n"
    		+ "tree_learner = serial\n"
    		+ "\n"
    		+ "# number of threads for multi-threading. One thread will use one CPU, defalut is setted to #cpu. \n"
    		+ "# num_threads = 8\n"
    		+ "\n"
    		+ "# feature sub-sample, will random select 80% feature to train on each iteration \n"
    		+ "# alias: sub_feature\n"
    		+ "feature_fraction = " + featureFraction + "\n"
    		+ "\n"
    		+ "# Support bagging (data sub-sample), will perform bagging every 5 iterations\n"
    		+ "bagging_freq = 1\n"
    		+ "\n"
    		+ "# Bagging fraction, will random select 80% data on bagging\n"
    		+ "# alias: sub_row\n"
    		+ "bagging_fraction = 0.9\n"
    		+ "\n"
    		+ "# minimal number data for one leaf, use this to deal with over-fit\n"
    		+ "# alias : min_data_per_leaf, min_data\n"
    		+ "min_data_in_leaf = 50\n"
    		+ "\n"
    		+ "# minimal sum Hessians for one leaf, use this to deal with over-fit\n"
    		+ "min_sum_hessian_in_leaf = 5.0\n"
    		+ "\n"
    		+ "# save memory and faster speed for sparse feature, alias: is_sparse\n"
    		+ "is_enable_sparse = true\n"
    		+ "\n"
    		+ "# when data is bigger than memory size, set this to true. otherwise set false will have faster speed\n"
    		+ "# alias: two_round_loading, two_round\n"
    		+ "use_two_round_loading = false\n"
    		+ "\n"
    		+ "# true if need to save data to binary file and application will auto load data from binary file next time\n"
    		+ "# alias: is_save_binary, save_binary\n"
    		+ "is_save_binary_file = false\n"
    		+ "\n"
    		+ "# output model file\n"
    		+ "output_model = LightGBM_model.txt\n";
  }
}
